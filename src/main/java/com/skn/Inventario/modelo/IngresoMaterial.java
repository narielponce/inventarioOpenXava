package com.skn.Inventario.modelo;

import lombok.Data;
import org.openxava.annotations.DescriptionsList;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.Required;
import org.openxava.jpa.XPersistence;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
public class IngresoMaterial implements Serializable {

    @Id
    @Column(length = 6)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private int idIngreso;

    @Column
    @Required
    private Date fecha;

    //@ManyToOne
    //@Required
    //private TipoTransaccion tipoTransaccion;

    @ManyToOne
    //@Required
    @NotNull
    private Material material;

    @Column
    @Required
    private int cantidad;

    @ManyToOne
    @DescriptionsList
    private Deposito deposito;

    @ManyToOne
    @Required
    @DescriptionsList
    private Usuario usuario;

    @PrePersist
    private void actualizarInventario() {
        Query query = XPersistence.getManager()
                .createQuery("SELECT i FROM Inventario i WHERE i.material = :material AND i.deposito = :deposito");
        query.setParameter("material", material);
        query.setParameter("deposito", deposito);

        Inventario inventario;
        try {
            inventario = (Inventario) query.getSingleResult();
            inventario.setCantidad(inventario.getCantidad() + cantidad);
        } catch (NoResultException e) {
            inventario = new Inventario();
            inventario.setMaterial(material);
            inventario.setDeposito(deposito);
            inventario.setCantidad(cantidad);
        }
        XPersistence.getManager().merge(inventario);
    }
    // Otros atributos como tipo de transacción, fecha, etc.
}
