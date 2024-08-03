package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "brand_image")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandImage implements Serializable {
    @Id
    @Column(name = "image_id")
    @UuidGenerator
    private UUID imageID;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @Lob
    @Column(name = "image_url", columnDefinition = "LONGTEXT", unique = false, nullable = true)
    private byte[] imageUrl;

    @Column(name = "image_description", columnDefinition = "varchar(255)", unique = false, nullable = true)
    private String imageDescription;

    private Boolean status;
}
