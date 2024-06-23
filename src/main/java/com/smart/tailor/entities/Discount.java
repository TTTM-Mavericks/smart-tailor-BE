package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "discount")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Discount extends AuditEntity implements Serializable {
    @Id
    @Column(name = "discount_id")
    @UuidGenerator
    private UUID discountID;

    @Column(name = "discount_percent", nullable = false, unique = false)
    private Double discountPercent;

    private Integer quantity;
}
