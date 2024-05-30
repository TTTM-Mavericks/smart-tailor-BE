package com.smart.tailor.entities;

import com.smart.tailor.enums.TypeOfUpgrade;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "brand_upgrade")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandUpgrade extends AuditEntity implements Serializable {
    @Id
    @Column(name = "brand_upgrade_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID brandUpgradeID;

    @OneToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "brand_id")
    private Brand brand;

    @Column(name = "type_of_upgrade")
    @Enumerated(EnumType.STRING)
    private TypeOfUpgrade typeOfUpgrade;
}
