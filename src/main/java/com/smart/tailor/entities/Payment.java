package com.smart.tailor.entities;

import com.smart.tailor.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "payment")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment extends AuditEntity implements Serializable {
    @Id
    @Column(name = "payment_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID paymentID;

    @Column(name = "payment_sender_id")
    private UUID paymentSenderID;

    private String paymentSenderName;

    private String paymentSenderBankCode;

    private String paymentSenderBankNumber;

    @Column(name = "payment_recipient_id")
    private UUID paymentRecipientID;

    private String paymentRecipientName;

    private String paymentRecipientBankCode;

    private String paymentRecipientBankNumber;

    private Double paymentAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "payment_status")
    private Boolean paymentStatus;
}
