package com.smart.tailor.service.impl;

import com.smart.tailor.constant.ErrorConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Brand;
import com.smart.tailor.entities.BrandImage;
import com.smart.tailor.enums.BrandStatus;
import com.smart.tailor.enums.UserStatus;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.BrandMapper;
import com.smart.tailor.repository.BrandRepository;
import com.smart.tailor.service.BrandImageService;
import com.smart.tailor.service.BrandService;
import com.smart.tailor.service.UserService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.BrandImageRequest;
import com.smart.tailor.utils.request.BrandRequest;
import com.smart.tailor.utils.response.BrandResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final UserService userService;
    private final BrandImageService brandImageService;
    private final BrandMapper brandMapper;
    private final Logger logger = LoggerFactory.getLogger(BrandServiceImpl.class);

    @Override
    public Optional<Brand> getBrandById(UUID brandId) {
        if (brandId == null || brandId.toString().isEmpty()) {
            throw new IllegalArgumentException(MessageConstant.MISSING_ARGUMENT);
        }
        return brandRepository.findById(brandId)
                .or(() -> {
                    var user = userService.getUserByUserID(brandId);
                    return user.isPresent()
                            ? Optional.of(Brand.builder().brandID(brandId).brandStatus(BrandStatus.PENDING).build())
                            : Optional.empty();
                });
    }

    @Override
    public Brand saveBrand(UUID brandID, BrandRequest brandRequest) {
        var user = userService.getUserByUserID(brandID)
                .orElseThrow(() -> new BadRequestException(MessageConstant.CAN_NOT_FIND_BRAND));

        if (user.getUserStatus() == UserStatus.INACTIVE) {
            throw new BadRequestException(ErrorConstant.ACCOUNT_NOT_VERIFIED.getMessage());
        }

        Brand savedBrand = brandRepository.save(
                Brand.builder()
                        .user(user)
                        .brandName(brandRequest.getBrandName())
                        .bankName(brandRequest.getBankName())
                        .accountNumber(brandRequest.getAccountNumber())
                        .accountName(brandRequest.getAccountName())
                        .brandStatus(BrandStatus.PENDING)
                        .address(brandRequest.getAddress())
                        .province(brandRequest.getProvince())
                        .ward(brandRequest.getWard())
                        .district(brandRequest.getDistrict())
                        .QR_Payment(brandRequest.getQrPayment())
                        .rating(1.0f)
                        .numberOfRatings(1)
                        .totalRatingScore(1.0f)
                        .numberOfViolations(0)
                        .build()
        );

        // Save images
        if (brandRequest.getBrandImages() != null) {
            for (BrandImageRequest imageRequest : brandRequest.getBrandImages()) {
                byte[] base64ImageUrl = null;
                if (Optional.ofNullable(imageRequest.getImageUrl()).isPresent()) {
                    base64ImageUrl = Utilities.encodeStringToBase64(imageRequest.getImageUrl());
                }
                BrandImage brandImage = BrandImage.builder()
                        .brand(savedBrand)
                        .imageUrl(base64ImageUrl)
                        .imageDescription(imageRequest.getImageDescription())
                        .status(false)
                        .build();
                brandImageService.saveBrandImage(brandImage);
            }
        }
        return savedBrand;
    }

    @Override
    public Brand getBrandByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException(MessageConstant.MISSING_ARGUMENT);
        }
        return brandRepository.findBrandByUserEmail(email);
    }

    @Override
    public Brand updateBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Optional<Brand> findBrandById(UUID brandID) {
        return brandRepository.findById(brandID);
    }

    @Override
    public List<Brand> findAllBrandByExpertTailoringID(UUID expertTailoringID) {
        return brandRepository.findAllBrandByExpertTailoringID(expertTailoringID);
    }

    @Override
    public void ratingBrand(UUID brandID, Integer numberOfRating, Float ratingScore) {
        logger.info("Inside Rating Brand");
        var brand = findBrandById(brandID)
                .orElseThrow(() -> new ItemNotFoundException("Cannot find Brand with BrandID: " + brandID));

        var numberOfRatingsUpdate = brand.getNumberOfRatings() + numberOfRating;
        var totalRatingScoreUpdate = brand.getTotalRatingScore() + ratingScore;
        var ratingUpdate = totalRatingScoreUpdate / numberOfRatingsUpdate;
        brandRepository.updateBrandRatingAndScore(
                Math.max(ratingUpdate, 0),
                numberOfRatingsUpdate,
                totalRatingScoreUpdate,
                brandID
        );
    }

    @Override
    public BrandResponse findBrandInformationByBrandID(UUID brandID) {
        var brand = findBrandById(brandID)
                .orElseThrow(() -> new ItemNotFoundException("Cannot find Brand with BrandID: " + brandID));
        return brandMapper.mapperToBrandResponse(brand);
    }

    @Override
    public List<BrandImage> getBrandImage(UUID brandID) {
        var brand = findBrandById(brandID)
                .orElseThrow(() -> new ItemNotFoundException("Cannot find Brand with BrandID: " + brandID));
        return brandImageService.getBrandImagesByBrand(brand);
    }

    @Override
    public boolean changeBrandImageStatus(UUID imageId) {
        try {
            brandImageService.updateBrandImageStatusById(imageId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
