package com.smart.tailor.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sample_product_data")
public class SampleProductData {

    @Id
    @UuidGenerator
    @Column(name = "sample_model_id", nullable = false, unique = true)
    private UUID sampleModelID;

    @Column(name = "order_id", nullable = false)
    private UUID orderID;

    @Column(name = "brand_id", nullable = false)
    private UUID brandID;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "images", columnDefinition = "text")
    private String images;

    @Column(name = "video", columnDefinition = "text")
    private String video;
}
