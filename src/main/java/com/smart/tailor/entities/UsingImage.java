package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "using_image")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UsingImage extends AuditEntity implements Serializable {
    @Id
    @Column(name = "using_image_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID usingImageID;

    @ManyToOne
    @JoinColumn(name = "image_id", referencedColumnName = "image_id")
    private Image image;

    @Column(name = "relation_id")
    private UUID relationID;

    private String type;
}
