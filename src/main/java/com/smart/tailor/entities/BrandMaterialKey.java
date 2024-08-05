package com.smart.tailor.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandMaterialKey implements Serializable {
    @Column(name = "material_id", nullable = false)
    private String materialID;

    @Column(name = "brand_id", nullable = false)
    private String brandID;
}
