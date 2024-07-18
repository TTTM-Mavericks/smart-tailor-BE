package com.smart.tailor.mapper;

import com.smart.tailor.entities.Payment;
import com.smart.tailor.utils.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

public interface PaymentMapper {
    PaymentResponse mapperToPaymentResponse(Payment payment);
}

@RequiredArgsConstructor
@Component
class PaymentMapperImpl implements PaymentMapper {
    @Override
    public PaymentResponse mapperToPaymentResponse(Payment payment) {
        if (payment == null) {
            return null;
        }

        PaymentResponse.PaymentResponseBuilder paymentResponse = PaymentResponse.builder();

        paymentResponse.paymentID(payment.getPaymentID());
        paymentResponse.paymentSenderName(payment.getPaymentSenderName());
        paymentResponse.paymentSenderBankCode(payment.getPaymentSenderBankCode());
        paymentResponse.paymentSenderBankNumber(payment.getPaymentSenderBankNumber());
        paymentResponse.paymentRecipientName(payment.getPaymentRecipientName());
        paymentResponse.paymentRecipientBankCode(payment.getPaymentRecipientBankCode());
        paymentResponse.paymentRecipientBankNumber(payment.getPaymentRecipientBankNumber());
        paymentResponse.paymentAmount(payment.getPaymentAmount());
        paymentResponse.paymentMethod(payment.getPaymentMethod());
        paymentResponse.paymentStatus(payment.getPaymentStatus());
        paymentResponse.paymentType(payment.getPaymentType());
        paymentResponse.orderID(payment.getOrder() != null ? payment.getOrder().getOrderID() : null);

        return paymentResponse.build();
    }
}