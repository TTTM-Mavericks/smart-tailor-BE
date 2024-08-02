package com.smart.tailor.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "sample_product_data")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SampleProductData extends AuditEntity implements Serializable {
    @Id
    @UuidGenerator
    @Column(name = "sample_model_id", nullable = false, unique = true)
    private UUID sampleModelID;

//    @ManyToOne
//    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
//    @JsonBackReference
//    private Order order;

    @ManyToOne
    @JoinColumn(name = "stage_id", referencedColumnName = "stage_id")
    @JsonBackReference
    private OrderStage orderStage;

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "brand_id")
    @JsonBackReference
    private Brand brand;

    @Column(name = "description", columnDefinition = "varchar(255)")
    private String description;

    @Lob
    @Column(name = "image_url", columnDefinition = "LONGTEXT")
    private byte[] imageUrl;

    @Lob
    @Column(name = "video", columnDefinition = "LONGTEXT")
    private byte[] video;

    boolean status;
}
