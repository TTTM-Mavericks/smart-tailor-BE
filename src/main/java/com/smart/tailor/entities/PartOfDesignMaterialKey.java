package com.smart.tailor.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PartOfDesignMaterialKey implements Serializable {
    @Column(name = "material_id", nullable = false)
    private UUID materialID;

    @ManyToOne
    @JoinColumn(name = "material_id", referencedColumnName = "material_id", nullable = false, insertable = false, updatable = false)
    private Material material;

    @Column(name = "part_of_design_id", nullable = false)
    private UUID partOfDesignID;

    @ManyToOne
    @JoinColumn(name = "part_of_design_id", referencedColumnName = "part_of_design_id", nullable = false, insertable = false, updatable = false)
    private PartOfDesign partOfDesign;
}
