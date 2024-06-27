package com.smart.tailor.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class SizeExpertTailoringKey implements Serializable {
    @Column(name = "expert_tailoring_id", nullable = false)
    private UUID expertTailoringID;

    @ManyToOne
    @JoinColumn(name = "expert_tailoring_id", referencedColumnName = "expert_tailoring_id", nullable = false, insertable = false, updatable = false)
    private ExpertTailoring expertTailoring;

    @Column(name = "size_id", nullable = false)
    private UUID sizeID;

    @ManyToOne
    @JoinColumn(name = "size_id", referencedColumnName = "size_id", nullable = false, insertable = false, updatable = false)
    private Size size;
}
