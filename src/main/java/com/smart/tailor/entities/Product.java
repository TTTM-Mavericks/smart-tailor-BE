package com.smart.tailor.entities;

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
@Table(name = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Product extends AuditEntity implements Serializable {
    @Id
    @Column(name = "product_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID productID;

    @ManyToOne
    @JoinColumn(name = "design_id", referencedColumnName = "design_id")
    private Design design;

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "brand_id")
    private Brand brand;

    @Column(name = "product_name")
    private String productName;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "price_per_product")
    private Double pricePerProduct;

    private Boolean gender;

    private Integer rating;

    @Column(name = "public_status")
    private Boolean publicStatus;

    private String size;
}
