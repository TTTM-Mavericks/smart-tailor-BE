package com.smart.tailor.entities;

import com.smart.tailor.utils.Utilities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;


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
    private String laborQuantityID;

    private Integer laborQuantityMinQuantity;

    private Integer laborQuantityMaxQuantity;

    private Integer laborQuantityMinPrice;

    private Integer laborQuantityMaxPrice;

    private Boolean status;

    @PrePersist
    private void prePersist() {
        this.laborQuantityID = Utilities.generateCustomPrimaryKey();
    }
}
