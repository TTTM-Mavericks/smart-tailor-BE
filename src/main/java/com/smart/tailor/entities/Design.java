package com.smart.tailor.entities;

import com.smart.tailor.utils.Utilities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "design")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Design extends AuditEntity implements Serializable {
    @Id
    @Column(name = "design_id", unique = true, nullable = false)
    private String designID;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "expert_tailoring_id", referencedColumnName = "expert_tailoring_id")
    private ExpertTailoring expertTailoring;

    @Column(name = "title_design", columnDefinition = "varchar(50) CHARACTER SET utf8 COLLATE utf8_bin")
    private String titleDesign;

    @Column(name = "public_status")
    private Boolean publicStatus;

    @Column(name = "min_weight")
    private Float minWeight;

    @Column(name = "max_weight")
    private Float maxWeight;

    @Lob
    @Column(name = "image_url", columnDefinition = "LONGTEXT")
    private byte[] imageUrl;

    @Column(name = "color", columnDefinition = "varchar(10)")
    private String color;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "design")
    private List<PartOfDesign> partOfDesignList;

    @PrePersist
    private void prePersist() {
        this.designID = Utilities.generateCustomPrimaryKey();
    }
}
