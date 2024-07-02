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

    @Column(name = "brand_id", nullable = false)
    private UUID brandID;
}
