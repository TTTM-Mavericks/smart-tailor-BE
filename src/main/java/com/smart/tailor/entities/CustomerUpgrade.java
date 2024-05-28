package com.smart.tailor.entities;

import com.smart.tailor.enums.TypeOfUpgrade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "customer_upgrade")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CustomerUpgrade extends AuditEntity implements Serializable {
    @Id
    @Column(name = "customer_upgrade_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID customerUpgradeID;

    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;

    @Column(name = "type_of_upgrade")
    @Enumerated(EnumType.STRING)
    private TypeOfUpgrade typeOfUpgrade;

    @Column(name = "upgrade_date_time")
    private LocalDateTime upgradeDateTime;
}
