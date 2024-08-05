package com.smart.tailor.entities;

import com.smart.tailor.utils.Utilities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;


@Entity
@Table(name = "category")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Category extends AuditEntity implements Serializable {
    @Id
    @Column(name = "category_id", unique = true, nullable = false)
    private String categoryID;

    @Column(name = "category_name", columnDefinition = "varchar(50) CHARACTER SET utf8 COLLATE utf8_bin")
    private String categoryName;

    private Boolean status;

    @PrePersist
    private void prePersist() {
        this.categoryID = Utilities.generateCustomPrimaryKey();
    }
}
