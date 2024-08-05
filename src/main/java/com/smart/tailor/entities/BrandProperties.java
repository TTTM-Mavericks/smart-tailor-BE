package com.smart.tailor.entities;

import com.smart.tailor.utils.Utilities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.io.Serializable;

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
    private String brandPropertyID;

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "brand_id", nullable = false, unique = false)
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "property_id", referencedColumnName = "property_id", nullable = false, unique = false)
    private SystemProperties systemProperties;

    @Column(name = "brand_property_value", columnDefinition = "varchar(150)")
    private String brandPropertyValue;

    @Column(name = "brand_property_status")
    private Boolean brandPropertyStatus;

    @PrePersist
    private void prePersist() {
        this.brandPropertyID = Utilities.generateCustomPrimaryKey();
    }
}
