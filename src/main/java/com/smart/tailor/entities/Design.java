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
@Table(name = "design")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Design extends AuditEntity implements Serializable {
    @Id
    @Column(name = "design_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID designID;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "expert_tailoring_name")
    private String expertTailoringName;

    @Column(name = "title_design")
    private String titleDesign;

    @Column(name = "public_status")
    private Boolean publicStatus;
}
