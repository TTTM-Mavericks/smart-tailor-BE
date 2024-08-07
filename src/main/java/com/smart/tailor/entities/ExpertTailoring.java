package com.smart.tailor.entities;

import com.smart.tailor.utils.Utilities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;


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
    @Column(name = "expert_tailoring_id", columnDefinition = "varchar(14)")
    private String expertTailoringID;

    @Column(name = "expert_tailoring_name", columnDefinition = "varchar(50) CHARACTER SET utf8 COLLATE utf8_bin")
    private String expertTailoringName;

    @Lob
    @Column(name = "size_image_url", columnDefinition = "LONGTEXT")
    private String sizeImageUrl;

    @Lob
    @Column(name = "model_image_url", columnDefinition = "LONGTEXT")
    private String modelImageUrl;

    private Boolean status;

    @PrePersist
    private void prePersist() {
        this.expertTailoringID = Utilities.generateCustomPrimaryKey();
    }
}
