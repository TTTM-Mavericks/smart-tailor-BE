package com.smart.tailor.entities;

import com.smart.tailor.enums.Provider;
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
    @Column(name = "brand_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID brandID;

    @OneToOne
    @MapsId
    @JoinColumn(name = "brand_id", referencedColumnName = "user_id")
    private User user;

    private String brandName;

    private Float rating;

    @Column(name = "bank_name")
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

    @Column(name = "number_of_violations")
    private Integer numberOfViolations;

    @ManyToOne
    @JoinColumn(name = "expert_tailoring_id", referencedColumnName = "expert_tailoring_id")
    private ExpertTailoring expertTailoring;
}
