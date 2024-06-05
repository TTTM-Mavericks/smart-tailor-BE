package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "item_mask")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemMask extends AuditEntity implements Serializable {
    @Id
    @Column(name = "item_mask_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID itemMaskID;

    @ManyToOne
    @JoinColumn(name = "part_of_design_id", referencedColumnName = "part_of_design_id")
    private PartOfDesign partOfDesign;

    @Column(name = "item_mask_name")
    private String itemMaskName;

    @Column(name = "type_of_item")
    private String typeOfItem;

    @Column(name = "position_x")
    private Float positionX;

    @Column(name = "position_y")
    private Float positionY;

    @Column(name = "scale_x")
    private Float scaleX;

    @Column(name = "scale_y")
    private Float scaleY;

    @Lob
    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;
}