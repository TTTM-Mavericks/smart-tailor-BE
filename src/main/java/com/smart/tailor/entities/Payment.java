package com.smart.tailor.entities;

import com.smart.tailor.enums.PaymentMethod;
import com.smart.tailor.enums.PaymentType;
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

    @Column(name = "payment_sender_id", insertable = false, updatable = false)
    private UUID paymentSenderID;

    @ManyToOne
    @JoinColumn(name = "payment_sender_id", referencedColumnName = "user_id", nullable = false, unique = false)
    private User paymentSender;

    @Column(name = "payment_sender_name")
    private String paymentSenderName;

    @Column(name = "payment_sender_bank_code")
    private String paymentSenderBankCode;

    @Column(name = "payment_sender_bank_number")
    private String paymentSenderBankNumber;

    @Column(name = "payment_recipient_id", insertable = false, updatable = false)
    private UUID paymentRecipientID;

    @ManyToOne
    @JoinColumn(name = "payment_recipient_id", referencedColumnName = "user_id", nullable = false, unique = false)
    private User paymentRecipient;

    @Column(name = "payment_recipient_name")
    private String paymentRecipientName;

    @Column(name = "payment_recipient_bank_code")
    private String paymentRecipientBankCode;

    @Column(name = "payment_recipient_bank_number")
    private String paymentRecipientBankNumber;

    @Column(name = "payment_amount")
    private Double paymentAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "payment_status")
    private Boolean paymentStatus;

    @Column(name = "order_id", insertable = false, updatable = false)
    private UUID orderID;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = true, unique = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType paymentType;
}
