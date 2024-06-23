package com.smart.tailor.entities;

import com.smart.tailor.enums.BrandStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "brand")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Brand extends AuditEntity implements Serializable {
    @Id
    @Column(name = "brand_id")
    @UuidGenerator
    private UUID brandID;

    @OneToOne
    @MapsId
    @JoinColumn(name = "brand_id", referencedColumnName = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;

    @Column(name = "brand_name", columnDefinition = "nvarchar(255)", unique = false, nullable = false)
    private String brandName;

    private Float rating;

    @Column(name = "bank_name", unique = false, nullable = true)
    private String bankName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "qr_payment")
    private String QR_Payment;

    private String address;

    private String province;

    private String district;

    private String ward;

    @Column(name = "brand_status")
    @Enumerated(EnumType.STRING)
    private BrandStatus brandStatus;

    @Column(name = "number_of_violations")
    private Integer numberOfViolations = 0;
}
