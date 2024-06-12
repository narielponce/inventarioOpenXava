package com.skn.Inventario.modelo;
import lombok.Data;
import org.openxava.annotations.DescriptionsList;
import org.openxava.annotations.Hidden;

import javax.persistence.*;

@Entity
@Data
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Hidden
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @DescriptionsList
    private Material material;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @DescriptionsList
    private Ubicacion ubicacion;

    private int cantidad;
}
