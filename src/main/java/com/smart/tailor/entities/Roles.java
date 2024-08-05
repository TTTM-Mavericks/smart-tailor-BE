package com.smart.tailor.entities;


import com.smart.tailor.utils.Utilities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;


@Entity
@Table(name = "roles")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Roles extends AuditEntity implements Serializable {
    @Id
    @Column(name = "role_id", unique = true, nullable = false)
    private String roleID;

    @Column(name = "role_name", nullable = false, columnDefinition = "varchar(20)")
    private String roleName;

    @PrePersist
    private void prePersist() {
        this.roleID = Utilities.generateCustomPrimaryKey();
    }
}
