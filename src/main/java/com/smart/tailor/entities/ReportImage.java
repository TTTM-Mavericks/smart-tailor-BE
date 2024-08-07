package com.smart.tailor.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.smart.tailor.utils.Utilities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;


@Entity
@Table(name = "report_image")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportImage extends AuditEntity implements Serializable {
    @Id
    @Column(name = "report_image_id", columnDefinition = "varchar(14)")
    private String reportImageID;

    @Column(name = "report_image_name", columnDefinition = "varchar(50) CHARACTER SET utf8 COLLATE utf8_bin")
    private String reportImageName;

    @Lob
    @Column(name = "report_image_url", columnDefinition = "LONGTEXT")
    private byte[] reportImageUrl;

    @ManyToOne
    @JoinColumn(name = "report_id", referencedColumnName = "report_id")
    @JsonBackReference
    private Report report;

    @PrePersist
    private void prePersist() {
        this.reportImageID = Utilities.generateCustomPrimaryKey();
    }
}
