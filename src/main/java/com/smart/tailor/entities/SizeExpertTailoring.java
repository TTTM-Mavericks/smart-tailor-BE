package com.smart.tailor.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
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

    private Double minFabric;

    private Double maxFabric;

    private Boolean status;
}
