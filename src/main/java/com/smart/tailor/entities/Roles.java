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
@Table(name = "roles")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Roles extends AuditEntity implements Serializable {
    @Id
    @Column(name = "role_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID roleID;

    @Column(name = "role_name", nullable = false)
    private String roleName;
}
