package com.smart.tailor.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExpertTailoringMaterialKey implements Serializable {
    @Column(name = "expert_tailoring_id", nullable = false)
    private UUID expertTailoringID;

    @Column(name = "material_id", nullable = false)
    private UUID materialID;
}
