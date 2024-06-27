package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "brand_properties")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class BrandProperties extends AuditEntity implements Serializable {
    @Id
    @Column(name = "brand_property_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID brandPropertyID;

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "brand_id", nullable = false, insertable = false, updatable = false)
    private Brand brand;

    @Column(name = "brand_property_name", nullable = false, unique = true)
    private String brandPropertyName;

    @Column(name = "brand_property_detail", nullable = false, unique = false)
    private String brandPropertyDetail;

    @Column(name = "brand_property_unit", nullable = true, unique = false)
    private String brandPropertyUnit;

    @Column(name = "brand_property_type", nullable = false, unique = false)
    private String brandPropertyType;

    @Column(name = "brand_property_status")
    private Boolean brandPropertyStatus;
}
