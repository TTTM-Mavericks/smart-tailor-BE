package com.smart.tailor.service.impl;

import com.smart.tailor.config.CustomExeption;
import com.smart.tailor.constant.ErrorConstant;
import com.smart.tailor.repository.BrandExpertTailoringRepository;
import com.smart.tailor.service.BrandExpertTailoringService;
import com.smart.tailor.service.BrandService;
import com.smart.tailor.service.ExpertTailoringService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BrandExpertTailoringServiceImpl implements BrandExpertTailoringService {
    private final BrandExpertTailoringRepository brandExpertTailoringRepository;
    private final BrandService brandService;
    private final ExpertTailoringService expertTailoringService;
    private final Logger logger = LoggerFactory.getLogger(BrandExpertTailoringServiceImpl.class);

    @Override
    public Boolean addExpertTailoringForBrand(UUID brandID, UUID expertTailoringID) throws Exception {
        try {
            if (brandID == null || expertTailoringID == null) {
                throw new CustomExeption(ErrorConstant.MISSING_ARGUMENT);
            }
            var brand = brandService.getBrandById(brandID);
            if (brand.isEmpty()) {
                throw new CustomExeption(ErrorConstant.BAD_REQUEST);
            }
            var expectTailoring = expertTailoringService.getExpertTailoringByID(expertTailoringID);
            if (expectTailoring.isEmpty()) {
                throw new CustomExeption(ErrorConstant.BAD_REQUEST);
            }

            brandExpertTailoringRepository.createShortBrandExpertTailoring(
                    brandID,
                    expertTailoringID
            );

            var brandExpectTailoring = brandExpertTailoringRepository.getBrandExpertTailoringByBrandExpertTailoringKey_BrandIDAndBrandExpertTailoringKey_ExpertTailoringID(brandID, expertTailoringID);
            return brandExpectTailoring != null;
        } catch (Exception ex) {
            logger.error("ERROR IN BRAND EXPECT TAILORING SERVICE - ADD TAILORING FOR BRAND: {}", ex.getMessage());
            throw ex;
        }
    }
}
