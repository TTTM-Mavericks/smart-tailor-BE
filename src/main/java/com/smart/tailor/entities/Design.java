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
    @UuidGenerator
    private UUID designID;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "expert_tailoring_id", referencedColumnName = "expert_tailoring_id")
    private ExpertTailoring expertTailoring;

    @Column(name = "title_design")
    private String titleDesign;

    @Column(name = "public_status")
    private Boolean publicStatus;

    @Lob
    @Column(name = "image_url", columnDefinition = "LONGTEXT")
    private byte[] imageUrl;

    @Lob
    @Column(name = "color")
    private String color;

    @OneToMany(mappedBy = "design", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PartOfDesign> partOfDesignList;
}
