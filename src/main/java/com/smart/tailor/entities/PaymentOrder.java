package com.smart.tailor.entities;

import com.smart.tailor.enums.PaymentType;
import jakarta.persistence.*;
import jakarta.persistence.EmbeddedId;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@Entity
@Table(name = "payment_orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentOrder extends AuditEntity implements Serializable {
   @EmbeddedId
   private PaymentOrderKey paymentOrderKey;

   @Enumerated(EnumType.STRING)
   @Column(name = "payment_order_type")
   private PaymentType paymentOrderType;
}
