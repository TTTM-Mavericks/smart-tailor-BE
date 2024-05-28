package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "expert_tailoring")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ExpertTailoring extends AuditEntity implements Serializable {
    @Id
    @Column(name = "expert_tailoring_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID expertTailoringID;

    @Column(name = "expert_tailoring_name")
    private String expertTailoringName;
}
