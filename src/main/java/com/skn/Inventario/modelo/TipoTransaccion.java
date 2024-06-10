package com.skn.Inventario.modelo;

import lombok.Data;
import org.openxava.annotations.Required;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class TipoTransaccion implements Serializable {

    @Id
    @Column(length = 6)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTipoTransaccion;

    @Column(length=50) @Required
    private String nombre;
    // Otros atributos como dirección, contacto, etc.
}
