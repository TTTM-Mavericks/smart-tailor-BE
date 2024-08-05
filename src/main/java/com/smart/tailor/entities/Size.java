package com.smart.tailor.entities;

import com.smart.tailor.utils.Utilities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;


@Entity
@Table(name = "size")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Size extends AuditEntity implements Serializable {
    @Id
    @Column(name = "size_id", unique = true, nullable = false)
    private String sizeID;

    @Column(name = "size_name", columnDefinition = "varchar(5)")
    private String sizeName;

    private Boolean status;

    @PrePersist
    private void prePersist() {
        this.sizeID = Utilities.generateCustomPrimaryKey();
    }
}
