package com.smart.tailor.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smart.tailor.entities.Payment;
import com.smart.tailor.service.PayOSService;
import com.smart.tailor.utils.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

public interface PaymentMapper {
    PaymentResponse mapperToPaymentResponse(Payment payment) throws JsonProcessingException;
}

@RequiredArgsConstructor
@Component
class PaymentMapperImpl implements PaymentMapper {
    private final PayOSService payOSService;
    private final Logger logger = LoggerFactory.getLogger(PaymentMapperImpl.class);

    @Override
    public PaymentResponse mapperToPaymentResponse(Payment payment) throws JsonProcessingException {
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
        var order = payment.getOrder() != null ? payment.getOrder().getOrderID() : null;
        paymentResponse.orderID(order);
        logger.error("IN PAYMENT MAPPER - ORDER CODE: {}", payment.getPaymentCode());
        paymentResponse.payOSResponse(payOSService.getPaymentInfo(payment.getPaymentCode()));
        return paymentResponse.build();
    }
}