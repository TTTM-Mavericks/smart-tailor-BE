package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.UUID;

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
    @Column(name = "report_image_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID reportImageID;

    @Column(name = "report_image_name")
    private String reportImageName;

    @Lob
    @Column(name = "report_image_url", columnDefinition = "TEXT")
    private String reportImageUrl;

    @ManyToOne
    @JoinColumn(name = "report_id", referencedColumnName = "report_id")
    private Report reportID;
}
