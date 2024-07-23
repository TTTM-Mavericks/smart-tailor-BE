package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "system_image")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class SystemImage extends AuditEntity implements Serializable {
    @Id
    @UuidGenerator
    @Column(name = "image_id")
    private UUID imageID;

    @Column(name = "image_name", columnDefinition = "varchar(50) CHARACTER SET utf8 COLLATE utf8_bin")
    private String imageName;

    @Column(name = "image_url", columnDefinition = "LONGTEXT")
    private String imageURL;

    @Column(name = "image_status")
    private Boolean imageStatus;

    @Column(name = "image_type", columnDefinition = "varchar(50)")
    private String imageType;

    @Column(name = "is_premium")
    private Boolean isPremium;
}
