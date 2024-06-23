package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
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

    @Column(name = "property_name", nullable = false, unique = true)
    private String propertyName;

    @Column(name = "property_unit", nullable = true, unique = false)
    private String propertyUnit;

    @Column(name = "property_detail", nullable = false, unique = false)
    private String propertyDetail;

    @Column(name = "property_status")
    private Boolean propertyStatus;
}
