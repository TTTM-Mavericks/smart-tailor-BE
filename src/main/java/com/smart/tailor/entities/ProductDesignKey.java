package com.smart.tailor.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductDesignKey implements Serializable {
    @Column(name = "product_id", nullable = false)
    private Integer productID;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false, insertable = false, updatable = false)
    private Product product;

    @Column(name = "design_id", nullable = false)
    private Integer designID;

    @ManyToOne
    @JoinColumn(name = "design_id", referencedColumnName = "design_id", nullable = false, insertable = false, updatable = false)
    private Design design;
}
