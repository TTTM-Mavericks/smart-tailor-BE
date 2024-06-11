package com.smart.tailor.entities;

import com.smart.tailor.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order extends AuditEntity implements Serializable {
    @Id
    @Column(name = "order_id", nullable = false, unique = true)
    @UuidGenerator
    private UUID orderID;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private String orderType;

    private String address;

    private String province;

    private String district;

    private String ward;

    private String phone;

    private String userName;

    private Double totalPrice;

    private Double depositPrice;

    private LocalDateTime expectedStartDate;

    private LocalDateTime expectedProductCompletionDate;

    private LocalDateTime estimatedDeliveryDate;

    private LocalDateTime productionStartDate;

    private LocalDateTime productionCompletionDate;
}
