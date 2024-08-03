package com.smart.tailor.service;

import com.smart.tailor.entities.Brand;
import com.smart.tailor.entities.BrandImage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BrandImageService {
    BrandImage saveBrandImage(BrandImage brandImage);

    List<BrandImage> saveAllBrandImages(List<BrandImage> brandImages);

    Optional<BrandImage> getBrandImageById(UUID imageId);

    void deleteBrandImageById(UUID imageId);

    List<BrandImage> getBrandImagesByBrand(Brand brand);

    void updateBrandImageStatusById(UUID imageId);
}
