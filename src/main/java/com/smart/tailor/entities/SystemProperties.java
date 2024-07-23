package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "system_properties")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class SystemProperties extends AuditEntity implements Serializable {
    @Id
    @Column(name = "property_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID propertyID;

    @Column(name = "property_name", columnDefinition = "varchar(50)", nullable = false, unique = true)
    private String propertyName;

    @Column(name = "property_unit", columnDefinition = "varchar(50)", nullable = true, unique = false)
    private String propertyUnit;

    @Column(name = "property_detail", columnDefinition = "varchar(255)", nullable = true, unique = false)
    private String propertyDetail;

    @Column(name = "property_type", columnDefinition = "varchar(50)", nullable = false, unique = false)
    private String propertyType;

    @Column(name = "property_value", columnDefinition = "varchar(100)", nullable = true, unique = false)
    private String propertyValue;

    @Column(name = "property_status")
    private Boolean propertyStatus;

    @OneToMany(mappedBy = "systemProperties")
    private List<BrandProperties> brandProperties;
}
