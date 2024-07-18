package com.smart.tailor.utils.response;

import com.smart.tailor.enums.PaymentMethod;
import com.smart.tailor.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private UUID paymentID;

    private UUID paymentSenderID;
    private String paymentSenderName;
    private String paymentSenderBankCode;
    private String paymentSenderBankNumber;

    private UUID paymentRecipientID;
    private String paymentRecipientName;
    private String paymentRecipientBankCode;
    private String paymentRecipientBankNumber;

    private Integer paymentAmount;
    private PaymentMethod paymentMethod;
    private Boolean paymentStatus;
    private PaymentType paymentType;

    private UUID orderID;
    private PayOSResponse payOSResponse;
}

