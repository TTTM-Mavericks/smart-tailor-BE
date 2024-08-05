package com.smart.tailor.entities;

import com.smart.tailor.enums.BrandStatus;
import com.smart.tailor.utils.Utilities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.io.Serializable;
import java.util.List;


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
    private String brandID;

    @OneToOne
    @MapsId
    @JoinColumn(name = "brand_id", referencedColumnName = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;

    @Column(name = "brand_name", columnDefinition = "varchar(50) CHARACTER SET utf8 COLLATE utf8_bin", unique = false, nullable = false)
    private String brandName;

    @Column(name = "bank_name", columnDefinition = "varchar(100)", unique = false, nullable = true)
    private String bankName;

    @Column(columnDefinition = "varchar(50)")
    private String accountNumber;

    @Column(name = "account_name", columnDefinition = "varchar(100)")
    private String accountName;

    @Column(name = "qr_payment", columnDefinition = "varchar(255)")
    private String QR_Payment;

    @Column(columnDefinition = "varchar(255)")
    private String address;

    @Column(columnDefinition = "varchar(100)")
    private String province;

    @Column(columnDefinition = "varchar(100)")
    private String district;

    @Column(columnDefinition = "varchar(100)")
    private String ward;

    @Column(name = "brand_status")
    @Enumerated(EnumType.STRING)
    private BrandStatus brandStatus;

    @Column(name = "number_of_violations")
    private Integer numberOfViolations = 0;

    private Float rating = 1.0f;

    private Integer numberOfRatings = 1;

    private Float totalRatingScore = 1.0f;

    private String taxCode;

    @OneToMany(mappedBy = "brand")
    private List<BrandProperties> brandProperties;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BrandImage> brandImages;

    @PrePersist
    private void prePersist() {
        this.brandID = Utilities.generateCustomPrimaryKey();
    }
}
