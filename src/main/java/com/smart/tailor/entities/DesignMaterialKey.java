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
public class DesignMaterialKey implements Serializable {
    @Column(name = "material_id", nullable = false)
    private UUID materialID;

    @ManyToOne
    @JoinColumn(name = "material_id", referencedColumnName = "material_id", nullable = false, insertable = false, updatable = false)
    private Material material;

    @Column(name = "design_id", nullable = false)
    private UUID designID;

    @ManyToOne
    @JoinColumn(name = "design_id", referencedColumnName = "design_id", nullable = false, insertable = false, updatable = false)
    private Design design;
}
