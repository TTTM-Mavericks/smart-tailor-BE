package com.smart.tailor.service.impl;

import com.smart.tailor.constant.ErrorConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Brand;
import com.smart.tailor.entities.User;
import com.smart.tailor.enums.BrandStatus;
import com.smart.tailor.enums.Provider;
import com.smart.tailor.enums.UserStatus;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.BrandMapper;
import com.smart.tailor.mapper.UserMapper;
import com.smart.tailor.repository.BrandRepository;
import com.smart.tailor.service.BrandService;
import com.smart.tailor.service.EmailSenderService;
import com.smart.tailor.service.RoleService;
import com.smart.tailor.service.UserService;
import com.smart.tailor.utils.request.BrandRequest;
import com.smart.tailor.utils.request.UserRequest;
import com.smart.tailor.utils.response.BrandResponse;
import com.smart.tailor.utils.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final UserService userService;
    private final BrandMapper brandMapper;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Override
    public Optional<Brand> getBrandById(UUID brandId) throws Exception {
        try {
            if (brandId == null || brandId.toString().isEmpty() || brandId.toString().isBlank()) {
                throw new Exception(MessageConstant.MISSING_ARGUMENT);
            }
            var brand = brandRepository.getBrandByBrandID(brandId);
            if (brand.isEmpty()) {
                var user = userService.getUserByUserID(brandId);
                if (user.isPresent()) {
                    return Optional.ofNullable(
                            Brand.builder()
                                    .brandID(brandId)
                                    .brandStatus(BrandStatus.PENDING)
                                    .build()
                    );
                } else {
                    return Optional.empty();
                }
            } else {
                return brand;
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public Brand saveBrand(UUID brandID, BrandRequest brandRequest) throws Exception {
        Brand savedBrand = null;
        try {
            var checkUser = userService.getUserByUserID(brandID);
            if (checkUser.isEmpty()) {
                throw new BadRequestException(MessageConstant.CAN_NOT_FIND_BRAND);
            }
            var user = checkUser.get();
            if (user.getUserStatus().equals(UserStatus.INACTIVE)) {
                throw new BadRequestException(ErrorConstant.ACCOUNT_NOT_VERIFIED.getMessage());
            } else {
                savedBrand = brandRepository.save(
                        Brand.builder()
                                .user(user)
                                .brandName(brandRequest.getBrandName())
                                .bankName(brandRequest.getBankName() != null && !brandRequest.getBrandName().trim().isEmpty() ? brandRequest.getAccountName() : null)
                                .accountNumber(brandRequest.getAccountNumber() != null && !brandRequest.getAccountNumber().trim().isEmpty() ? brandRequest.getAccountNumber() : null)
                                .accountName(brandRequest.getAccountName() != null ? brandRequest.getAccountName() : null)
                                .brandStatus(BrandStatus.PENDING)
                                .address(brandRequest.getAddress() != null && !brandRequest.getAddress().trim().isEmpty() ? brandRequest.getAddress() : null)
                                .province(brandRequest.getProvince() != null && !brandRequest.getProvince().trim().isEmpty() ? brandRequest.getProvince() : null)
                                .ward(brandRequest.getWard() != null && !brandRequest.getWard().trim().isEmpty() ? brandRequest.getWard() : null)
                                .district(brandRequest.getDistrict() != null && !brandRequest.getDistrict().trim().isEmpty() ? brandRequest.getDistrict() : null)
                                .QR_Payment(brandRequest.getQrPayment() != null && !brandRequest.getQrPayment().trim().isEmpty() ? brandRequest.getQrPayment() : null)
                                .rating(1.0f)
                                .numberOfRatings(1)
                                .totalRatingScore(1.0f)
                                .numberOfViolations(0)
                                .build()
                );
            }
            return savedBrand;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public Brand getBrandByEmail(String email) throws Exception {
        try {
            if (email == null || email.isEmpty() || email.isBlank()) {
                throw new Exception(MessageConstant.MISSING_ARGUMENT);
            }
            return brandRepository.findBrandByUserEmail(email);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public Brand updateBrand(Brand brand) throws Exception {
        try {
            Brand savedBrand = brandRepository.save(brand);
            return savedBrand;
        } catch (Exception ex) {
            throw ex;
        }
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
                .orElseThrow(() -> new ItemNotFoundException("Can not find Brand with BrandID: " + brandID));

        var numberOfRatingsUpdate = brand.getNumberOfRatings() + numberOfRating;
        var totalRatingScoreUpdate = brand.getTotalRatingScore() + ratingScore;
        var ratingUpdate = totalRatingScoreUpdate / numberOfRatingsUpdate;
        brandRepository.updateBrandRatingAndScore(
                ratingUpdate <= 0 ? 0 : ratingUpdate,
                numberOfRatingsUpdate,
                totalRatingScoreUpdate,
                brandID
        );
    }

    @Override
    public BrandResponse findBrandInformationByBrandID(UUID brandID) {
        var brand = findBrandById(brandID)
                .orElseThrow(() -> new ItemNotFoundException("Can not find Brand with BrandID: " + brandID));

        return brandMapper.mapperToBrandResponse(brand);
    }
}
