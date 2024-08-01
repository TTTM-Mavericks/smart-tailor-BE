package com.smart.tailor.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.smart.tailor.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_order_id")
    private Order parentOrder;

    @OneToMany(mappedBy = "parentOrder", cascade = CascadeType.ALL)
    private Set<Order> subOrders;

    private Integer quantity;

    private Float rating;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "order_type", columnDefinition = "varchar(20)")
    private String orderType;

    @Column(columnDefinition = "varchar(255)")
    private String address;

    @Column(columnDefinition = "varchar(100)")
    private String province;

    @Column(columnDefinition = "varchar(100)")
    private String district;

    @Column(columnDefinition = "varchar(100)")
    private String ward;

    @Column(columnDefinition = "varchar(12)")
    private String phone;

    @Column(name = "buyer_name", columnDefinition = "varchar(50) CHARACTER SET utf8 COLLATE utf8_bin")
    private String buyerName;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Column(name = "expected_start_date", columnDefinition = "datetime(2)")
    private LocalDateTime expectedStartDate;

    @Column(name = "expected_product_completion_date", columnDefinition = "datetime(2)")
    private LocalDateTime expectedProductCompletionDate;

    @Column(name = "estimated_delivery_date", columnDefinition = "datetime(2)")
    private LocalDateTime estimatedDeliveryDate;

    @Column(name = "production_start_date", columnDefinition = "datetime(2)")
    private LocalDateTime productionStartDate;

    @Column(name = "product_completion_date", columnDefinition = "datetime(2)")
    private LocalDateTime productionCompletionDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<DesignDetail> detailList;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Payment> paymentList;

    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", quantity=" + quantity +
                ", orderStatus=" + orderStatus +
                ", orderType='" + orderType + '\'' +
                ", address='" + address + '\'' +
                ", province='" + province + '\'' +
                ", district='" + district + '\'' +
                ", ward='" + ward + '\'' +
                ", phone='" + phone + '\'' +
                ", buyerName='" + buyerName + '\'' +
                ", totalPrice=" + totalPrice +
                ", expectedStartDate=" + expectedStartDate +
                ", expectedProductCompletionDate=" + expectedProductCompletionDate +
                ", estimatedDeliveryDate=" + estimatedDeliveryDate +
                ", productionStartDate=" + productionStartDate +
                ", productionCompletionDate=" + productionCompletionDate +
                '}';
    }
}
