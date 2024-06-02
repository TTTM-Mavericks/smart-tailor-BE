package com.smart.tailor.entities;

import com.smart.tailor.enums.TypeOfUpgrade;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "brand_registration")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandRegistration extends AuditEntity implements Serializable {
    @Id
    @Column(name = "brand_registration_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID brandRegistrationID;

    @OneToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "brand_id")
    private Brand brand;

    @OneToOne
    @JoinColumn(name = "payment_id", referencedColumnName = "payment_id")
    private Payment payment;
}
