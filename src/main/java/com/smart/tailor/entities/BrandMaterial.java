package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.io.Serializable;

@Entity
@Table(name = "brand_material")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class BrandMaterial extends AuditEntity implements Serializable {
    @EmbeddedId
    private BrandMaterialKey brandMaterialKey;

    @ManyToOne
    @JoinColumn(name = "material_id", referencedColumnName = "material_id", nullable = false, insertable = false, updatable = false)
    private Material material;

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "brand_id", nullable = false, insertable = false, updatable = false)
    private Brand brand;

    @Column(name = "brand_price", nullable = false, unique = false)
    private Integer brandPrice;
}
