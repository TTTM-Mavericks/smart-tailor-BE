package com.smart.tailor.entities;

import com.smart.tailor.enums.MaskItemName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "mask_item")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class MaskItem extends AuditEntity implements Serializable {
    @Id
    @Column(name = "item_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID itemID;

    @ManyToOne
    @JoinColumn(name = "part_of_design_id", referencedColumnName = "part_of_design_id")
    private PartOfDesign partOfDesign;

    @Column(name = "mask_item_name")
    @Enumerated(EnumType.STRING)
    private MaskItemName maskItemName;

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
}
