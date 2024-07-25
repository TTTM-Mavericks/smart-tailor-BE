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
    PaymentResponse mapperToPaymentResponse(Payment payment);
}

@RequiredArgsConstructor
@Component
class PaymentMapperImpl implements PaymentMapper {
    private final PayOSService payOSService;
    private final Logger logger = LoggerFactory.getLogger(PaymentMapperImpl.class);

    @Override
    public PaymentResponse mapperToPaymentResponse(Payment payment) {
        if (payment == null) {
            return null;
        }

        try {
            return PaymentResponse.builder()
                    .paymentID(payment.getPaymentID())
                    .paymentSenderName(payment.getPaymentSenderName())
                    .paymentSenderBankCode(payment.getPaymentSenderBankCode())
                    .paymentSenderBankNumber(payment.getPaymentSenderBankNumber())
                    .paymentRecipientName(payment.getPaymentRecipientName())
                    .paymentRecipientBankCode(payment.getPaymentRecipientBankCode())
                    .paymentRecipientBankNumber(payment.getPaymentRecipientBankNumber())
                    .paymentAmount(payment.getPaymentAmount())
                    .paymentMethod(payment.getPaymentMethod())
                    .paymentStatus(payment.getPaymentStatus())
                    .paymentType(payment.getPaymentType())
                    .orderID(payment.getOrder() != null ? payment.getOrder().getOrderID() : null)
                    .payOSResponse(payOSService.getPaymentInfo(payment.getPaymentCode()))
                    .createDate(payment.getCreateDate().toString())
                    .build();
        } catch (JsonProcessingException e) {
            logger.error("Error processing payment JSON", e);
            throw new RuntimeException(e);
        }
    }
}