package com.smart.tailor.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smart.tailor.utils.request.PayOSRequest;
import com.smart.tailor.utils.response.PayOSResponse;

public interface PayOSService {
    PayOSResponse createPaymentLink(PayOSRequest paymentRequest) throws Exception;

    public PayOSResponse getPaymentInfo(Integer paymentID) throws JsonProcessingException;

    void confirmPayment(Integer orderCode) throws JsonProcessingException;
}
