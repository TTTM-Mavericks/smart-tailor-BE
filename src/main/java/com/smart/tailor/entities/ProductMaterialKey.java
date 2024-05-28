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
public class ProductMaterialKey implements Serializable {
    @Column(name = "product_id", nullable = false)
    private UUID productID;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false, insertable = false, updatable = false)
    private Product product;

    @Column(name = "material_id", nullable = false)
    private UUID materialID;

    @ManyToOne
    @JoinColumn(name = "material_id", referencedColumnName = "material_id", nullable = false, insertable = false, updatable = false)
    private Material material;
}
