package com.smart.tailor.service;

import com.smart.tailor.entities.Brand;
import com.smart.tailor.utils.request.BrandRequest;
import com.smart.tailor.utils.request.UserRequest;
import com.smart.tailor.utils.response.UserResponse;

import java.util.Optional;
import java.util.UUID;

public interface BrandService {
    Optional<Brand> getBrandById(UUID brandId) throws Exception;

    Brand saveBrand(BrandRequest brandRequest) throws Exception;

    public Boolean verifyUser(String email, String token) throws Exception;

    UserResponse register(UserRequest userRequest) throws Exception;

    Boolean checkVerify(String email);

    Optional<Brand> findBrandByBrandName(String brandName);

    Brand getBrandByEmail(String email) throws Exception;

    Brand updateBrand(Brand brand) throws Exception;
}
