package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;


@Entity
@Table(name = "customer")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Customer extends AuditEntity implements Serializable {
    @Id
    @Column(name = "customer_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID customerID;

    @OneToOne
    @MapsId
    @JoinColumn(name = "customer_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "full_name")
    private String fullName;

    private Boolean gender;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    private String address;

    private String province;

    private String district;

    @Column(name = "number_of_violations")
    private Integer numberOfViolations;
}
