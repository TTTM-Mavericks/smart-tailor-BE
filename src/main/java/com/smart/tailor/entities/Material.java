package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "material")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Material extends AuditEntity implements Serializable {
    @Id
    @Column(name = "material_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID materialID;

    private String materialName;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    @Column(name = "hs_code")
    private Double hsCode;

    private String unit;

    @Column(name = "base_price")
    private Double basePrice;

    private Boolean status;
}
