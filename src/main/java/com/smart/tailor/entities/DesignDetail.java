package com.smart.tailor.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.smart.tailor.utils.Utilities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;


@Entity
@Table(name = "design_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DesignDetail extends AuditEntity implements Serializable {
    @Id
    @Column(name = "design_detail_id", unique = true, nullable = false)
    private String designDetailID;

    @ManyToOne
    @JoinColumn(name = "design_id", referencedColumnName = "design_id", unique = false, nullable = false)
    private Design design;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", unique = false, nullable = true)
    @JsonBackReference
    private Order order;

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "brand_id", unique = false, nullable = true)
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "size_id", referencedColumnName = "size_id", unique = false, nullable = true)
    private Size size;

    private Integer quantity;

    private Boolean detailStatus;

    @PrePersist
    private void prePersist() {
        this.designDetailID = Utilities.generateCustomPrimaryKey();
    }
}
