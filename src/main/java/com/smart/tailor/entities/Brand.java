package com.smart.tailor.entities;

import com.smart.tailor.enums.Provider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@Entity
@Table(name = "brand")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Brand extends AuditEntity implements Serializable {
    @Id
    @Column(name = "brand_id", unique = true, nullable = false)
    private Integer brandID;

    @OneToOne
    @MapsId
    @JoinColumn(name = "brand_id", referencedColumnName = "user_id")
    private User user;

    private String brandName;

    private Float rating;

    private String bankName;

    private String accountNumber;

    private String accountName;

    @Column(name = "qr_payment")
    private String QR_Payment;
}
