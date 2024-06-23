package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "part_of_design")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PartOfDesign extends AuditEntity implements Serializable {
    @Id
    @Column(name = "part_of_design_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID partOfDesignID;

    @ManyToOne
    @JoinColumn(name = "design_id", referencedColumnName = "design_id")
    private Design design;

    @Column(name = "part_of_design_name")
    private String partOfDesignName;

    @Lob
    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Lob
    @Column(name = "success_image_url", columnDefinition = "TEXT")
    private String successImageUrl;

    @OneToMany(mappedBy = "partOfDesign", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemMask> itemMaskList;
}
