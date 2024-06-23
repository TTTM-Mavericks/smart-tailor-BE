package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExpertTailoring extends AuditEntity implements Serializable {
    @Id
    @Column(name = "expert_tailoring_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID expertTailoringID;

    @Column(name = "expert_tailoring_name")
    private String expertTailoringName;

    private Boolean status;
}
