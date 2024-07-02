package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@Entity
@Table(name = "brand_labor_quantity")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandLaborQuantity extends AuditEntity implements Serializable {
    @EmbeddedId
    private BrandLaborQuantityKey brandLaborQuantityKey;

    @ManyToOne
    @JoinColumn(name = "labor_quantity_id", referencedColumnName = "labor_quantity_id", nullable = false, insertable = false, updatable = false)
    private LaborQuantity laborQuantity;

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "brand_id", nullable = false, insertable = false, updatable = false)
    private Brand brand;

    private Double brandLaborCostPerQuantity;

    private Boolean status;
}
