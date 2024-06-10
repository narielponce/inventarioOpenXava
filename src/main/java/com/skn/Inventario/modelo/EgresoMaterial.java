package com.skn.Inventario.modelo;

import lombok.Data;
import org.openxava.annotations.DescriptionsList;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.Required;
import org.openxava.jpa.XPersistence;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
public class EgresoMaterial implements Serializable {

    @Id
    @Column(length = 6)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private int idEgreso;

    @Column
    @Required
    private Date fecha;

    //@ManyToOne
    //@Required
    //private TipoTransaccion tipoTransaccion;

    @ManyToOne
    @Required
    private Material material;

    @Column
    @Required
    private int cantidad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @DescriptionsList
    private Deposito depositoOrigen;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @DescriptionsList
    private Deposito depositoDestino;

    @ManyToOne
    @Required
    @DescriptionsList
    private Usuario usuario;

    @PrePersist
    private void actualizarInventario() {
        // Disminuir cantidad en el depósito de origen
        Query queryOrigen = XPersistence.getManager()
                .createQuery("SELECT i FROM Inventario i WHERE i.material = :material AND i.deposito = :deposito");
        queryOrigen.setParameter("material", material);
        queryOrigen.setParameter("deposito", depositoOrigen);

        Inventario inventarioOrigen;
        try {
            inventarioOrigen = (Inventario) queryOrigen.getSingleResult();
            int nuevaCantidad = inventarioOrigen.getCantidad() - cantidad;
            if (nuevaCantidad < 0) {
                throw new IllegalArgumentException("Cantidad insuficiente en el depósito de origen");
            }
            inventarioOrigen.setCantidad(nuevaCantidad);
            XPersistence.getManager().merge(inventarioOrigen);
        } catch (NoResultException e) {
            throw new IllegalArgumentException("No existe inventario para el material en el depósito de origen");
        }

        // Si hay depósito de destino, aumentar cantidad en el depósito de destino
        if (depositoDestino != null) {
            Query queryDestino = XPersistence.getManager()
                    .createQuery("SELECT i FROM Inventario i WHERE i.material = :material AND i.deposito = :deposito");
            queryDestino.setParameter("material", material);
            queryDestino.setParameter("deposito", depositoDestino);

            Inventario inventarioDestino;
            try {
                inventarioDestino = (Inventario) queryDestino.getSingleResult();
                inventarioDestino.setCantidad(inventarioDestino.getCantidad() + cantidad);
            } catch (NoResultException e) {
                inventarioDestino = new Inventario();
                inventarioDestino.setMaterial(material);
                inventarioDestino.setDeposito(depositoDestino);
                inventarioDestino.setCantidad(cantidad);
            }
            XPersistence.getManager().merge(inventarioDestino);
        }
    }
    // Otros atributos como tipo de transacción, fecha, etc.
}
