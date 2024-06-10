package com.skn.Inventario.modelo;

import lombok.Data;
import org.openxava.annotations.DescriptionsList;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.Required;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class Material implements Serializable {

    @Id
    @Column(length = 6)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private int idMaterial;

    @Column(length=50) @Required
    private String nombre;

    @Column(length=50)
    private String serialNumber;

    @ManyToOne
    @DescriptionsList
    private Fabricante fabricante;

    @ManyToOne
    @DescriptionsList
    private Categoria categoria;

    //@ManyToOne
    //@DescriptionsList
    //private Deposito deposito;

    // Otros atributos como cantidad, descripción, etc.
}
