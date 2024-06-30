package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Table(name = "system_image")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class SystemImage {
    @Id
    @UuidGenerator
    @Column(name = "image_id")
    private UUID imageID;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "image_url", columnDefinition = "LONGTEXT")
    private String imageURL;

    @Column(name = "image_status")
    private Boolean imageStatus;

    @Column(name = "image_type")
    private String imageType;

    @Column(name = "is_premium")
    private Boolean isPremium;
}
