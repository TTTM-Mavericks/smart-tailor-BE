package com.smart.tailor.service;

import com.smart.tailor.entities.Brand;
import com.smart.tailor.entities.BrandImage;
import com.smart.tailor.utils.request.BrandRequest;
import com.smart.tailor.utils.response.BrandResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BrandService {
    Optional<Brand> getBrandById(UUID brandId) throws Exception;

    Brand saveBrand(UUID brandID, BrandRequest brandRequest) throws Exception;

    Brand getBrandByEmail(String email) throws Exception;

    Brand updateBrand(Brand brand) throws Exception;

    Optional<Brand> findBrandById(UUID brandID);

    List<Brand> findAllBrandByExpertTailoringID(UUID expertTailoringID);

    void ratingBrand(UUID brandID, Integer numberOfRating, Float ratingScore);

    BrandResponse findBrandInformationByBrandID(UUID brandID);

    List<BrandImage> getBrandImage(UUID brandID);

    boolean changeBrandImageStatus(UUID imageId);
}
