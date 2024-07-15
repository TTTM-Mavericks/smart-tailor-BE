package com.smart.tailor.service;

import com.smart.tailor.utils.request.BrandPropertiesRequest;
import com.smart.tailor.utils.response.BrandPropertiesResponse;

import java.util.List;
import java.util.UUID;

public interface BrandPropertiesService {
    List<BrandPropertiesResponse> getAllByBrandID(UUID brandID);

    List<BrandPropertiesResponse> getAll();

    BrandPropertiesResponse getByID(UUID propertyID);

    BrandPropertiesResponse addNew(BrandPropertiesRequest brandRequest) throws Exception;
}
