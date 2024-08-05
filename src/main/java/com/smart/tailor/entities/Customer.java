package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;



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
    private String customerID;

    @OneToOne
    @MapsId
    @JoinColumn(name = "customer_id", referencedColumnName = "user_id")
    private User user;

    private Boolean gender;

    @Column(name = "date_of_birth", nullable = true)
    private Date dateOfBirth;

    @Column(columnDefinition = "varchar(255)")
    private String address;

    @Column(columnDefinition = "varchar(100)")
    private String province;

    @Column(columnDefinition = "varchar(100)")
    private String district;

    @Column(columnDefinition = "varchar(100)")
    private String ward;

    @Column(name = "number_of_violations")
    private Integer numberOfViolations = 0;
}
