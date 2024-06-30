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
public class BrandLaborQuantityKey implements Serializable {
    @Column(name = "labor_quantity_id", nullable = false)
    private UUID laborQuantityID;

    @ManyToOne
    @JoinColumn(name = "labor_quantity_id", referencedColumnName = "labor_quantity_id", nullable = false, insertable = false, updatable = false)
    private LaborQuantity laborQuantity;

    @Column(name = "brand_id", nullable = false)
    private UUID brandID;

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "brand_id", nullable = false, insertable = false, updatable = false)
    private Brand brand;
}
