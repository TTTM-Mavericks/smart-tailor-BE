package com.smart.tailor.service;

import com.smart.tailor.utils.request.PaymentRequest;
import com.smart.tailor.utils.response.PaymentResponse;

import java.util.UUID;

public interface PaymentService {
    PaymentResponse createPayOSPayment(PaymentRequest paymentRequest) throws Exception;

    PaymentResponse getPaymentByID(UUID paymentID) throws Exception;
}
