package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "labor_quantity")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class LaborQuantity extends AuditEntity implements Serializable {
    @Id
    @Column(name = "labor_quantity_id")
    @UuidGenerator
    private UUID laborQuantityID;

    private Integer laborQuantityMinQuantity;

    private Integer laborQuantityMaxQuantity;

    private Double laborQuantityMinPrice;

    private Double laborQuantityMaxPrice;

    private Boolean status;
}
