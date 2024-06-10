package com.skn.Inventario.modelo;

import lombok.Data;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.Required;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class Deposito implements Serializable {

    @Id
    @Column(length = 6)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private int idDeposito;

    @Column(length=50) @Required
    private String nombre;
    // Otros atributos como ubicación, descripción, etc.
}
