package com.smart.tailor.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "part_of_design")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "partOfDesignID")
public class PartOfDesign extends AuditEntity implements Serializable {
    @Id
    @Column(name = "part_of_design_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID partOfDesignID;

    @ManyToOne
    @JoinColumn(name = "design_id", referencedColumnName = "design_id", nullable = false, unique = false)
    private Design design;

    @Column(name = "part_of_design_name")
    private String partOfDesignName;

    @Lob
    @Column(name = "image_url", columnDefinition = "LONGTEXT")
    private byte[] imageUrl;

    @Lob
    @Column(name = "success_image_url", columnDefinition = "LONGTEXT")
    private byte[] successImageUrl;

    @OneToMany(mappedBy = "partOfDesign", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ItemMask> itemMaskList;

    @ManyToOne
    @JoinColumn(name = "material_id", referencedColumnName = "material_id")
    @JsonManagedReference
    private Material material;
}
