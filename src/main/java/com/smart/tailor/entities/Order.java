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
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    private Employee employee;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "order_type")
    private String orderType;

    private String address;

    private String province;

    private String district;

    private String ward;

    private String phone;

    @Column(name = "buyer_name")
    private String buyerName;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "expected_start_date")
    private LocalDateTime expectedStartDate;

    @Column(name = "expected_product_completion_date")
    private LocalDateTime expectedProductCompletionDate;

    @Column(name = "estimated_delivery_date")
    private LocalDateTime estimatedDeliveryDate;

    @Column(name = "production_start_date")
    private LocalDateTime productionStartDate;

    @Column(name = "product_completion_date")
    private LocalDateTime productionCompletionDate;
}
