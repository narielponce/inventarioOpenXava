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

    @ManyToOne
    @Required
    private Material material;

    @Column
    @Required
    private int cantidad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @DescriptionsList
    private Ubicacion origen;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @DescriptionsList
    private Ubicacion destino;

    @ManyToOne
    @Required
    @DescriptionsList
    private Usuario usuario;

    @PrePersist
    private void actualizarInventario() {
        // Disminuir cantidad en el depósito de origen
        Query queryOrigen = XPersistence.getManager()
                .createQuery("SELECT i FROM Inventario i WHERE i.material = :material AND i.ubicacion = :ubicacion");
        queryOrigen.setParameter("material", material);
        queryOrigen.setParameter("ubicacion", origen);

        Inventario inventarioOrigen;
        try {
            inventarioOrigen = (Inventario) queryOrigen.getSingleResult();
            int nuevaCantidad = inventarioOrigen.getCantidad() - cantidad;
            if (nuevaCantidad < 0) {
                throw new IllegalArgumentException("Cantidad insuficiente en la uibicación de origen");
            }
            inventarioOrigen.setCantidad(nuevaCantidad);
            XPersistence.getManager().merge(inventarioOrigen);
        } catch (NoResultException e) {
            throw new IllegalArgumentException("No existe inventario para el material en la ubicación de origen");
        }

        // Si hay depósito de destino, aumentar cantidad en el depósito de destino
        if (destino != null) {
            Query queryDestino = XPersistence.getManager()
                    .createQuery("SELECT i FROM Inventario i WHERE i.material = :material AND i.ubicacion = :ubicacion");
            queryDestino.setParameter("material", material);
            queryDestino.setParameter("ubicacion", destino);

            Inventario inventarioDestino;
            try {
                inventarioDestino = (Inventario) queryDestino.getSingleResult();
                inventarioDestino.setCantidad(inventarioDestino.getCantidad() + cantidad);
            } catch (NoResultException e) {
                inventarioDestino = new Inventario();
                inventarioDestino.setMaterial(material);
                inventarioDestino.setUbicacion(destino);
                inventarioDestino.setCantidad(cantidad);
            }
            XPersistence.getManager().merge(inventarioDestino);
        }
    }
}
