package com.smart.tailor.service;

import com.smart.tailor.entities.Payment;
import com.smart.tailor.utils.request.PaymentRequest;
import com.smart.tailor.utils.response.PaymentResponse;
import org.springframework.data.util.Pair;

import java.util.List;


public interface PaymentService {
    PaymentResponse createPayOSPayment(PaymentRequest paymentRequest) throws Exception;

    PaymentResponse createManualPayment(PaymentRequest paymentRequest) throws Exception;

    PaymentResponse getManualPaymentByID(String paymentID) throws Exception;

    PaymentResponse getPaymentByID(String paymentID) throws Exception;

    List<Payment> findAllByOrderID(String orderID);

    Payment updatePayment(Payment payment);

    List<Payment> getAllPayment();

    List<PaymentResponse> getAllPaymentResponse();

    List<PaymentResponse> getPaymentByUserID(String userID);

    Float calculatePaymentGrowthPercentageForCurrentAndPreviousWeek();

    List<Pair<String, Integer>> getTotalPaymentOfEachMonth();
}
