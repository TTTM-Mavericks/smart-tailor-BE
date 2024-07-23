package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@Entity
@Table(name = "size_expert_tailoring")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SizeExpertTailoring extends AuditEntity implements Serializable {
    @EmbeddedId
    private SizeExpertTailoringKey sizeExpertTailoringKey;

    @ManyToOne
    @JoinColumn(name = "expert_tailoring_id", referencedColumnName = "expert_tailoring_id", nullable = false, insertable = false, updatable = false)
    private ExpertTailoring expertTailoring;

    @ManyToOne
    @JoinColumn(name = "size_id", referencedColumnName = "size_id", nullable = false, insertable = false, updatable = false)
    private Size size;

    private Double minFabric;

    private Double maxFabric;

    @Column(columnDefinition = "varchar(50)")
    private String unit;

    private Boolean status;
}
