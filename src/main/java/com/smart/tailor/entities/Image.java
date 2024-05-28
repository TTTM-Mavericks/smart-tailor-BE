package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "image")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Image extends AuditEntity implements Serializable {
    @Id
    @Column(name = "image_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID imageID;

    private String name;

    @Lob
    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;
}
