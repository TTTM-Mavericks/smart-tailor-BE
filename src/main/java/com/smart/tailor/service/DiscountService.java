package com.smart.tailor.service;

import com.smart.tailor.utils.request.DiscountRequest;
import com.smart.tailor.utils.response.DiscountResponse;

import java.util.List;

public interface DiscountService {
    void addNewDiscount(DiscountRequest discountRequest);

    List<DiscountResponse> getAllValidDiscount();

    List<DiscountResponse> getAllDiscount();
}
