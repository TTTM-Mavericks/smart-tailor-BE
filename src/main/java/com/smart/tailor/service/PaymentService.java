package com.smart.tailor.service;

import com.smart.tailor.entities.Payment;
import com.smart.tailor.utils.request.PaymentRequest;
import com.smart.tailor.utils.response.PaymentResponse;

import java.util.List;
import java.util.UUID;

public interface PaymentService {
    PaymentResponse createPayOSPayment(PaymentRequest paymentRequest) throws Exception;

    PaymentResponse createManualPayment(PaymentRequest paymentRequest) throws Exception;

    PaymentResponse getManualPaymentByID(UUID paymentID) throws Exception;

    PaymentResponse getPaymentByID(UUID paymentID) throws Exception;

    List<Payment> findAllByOrderID(UUID orderID);

    Payment updatePayment(Payment payment);

    List<Payment> getAllPayment();
}
