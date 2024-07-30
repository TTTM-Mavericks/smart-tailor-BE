package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.SampleProductData;
import com.smart.tailor.repository.SampleProductDataRepository;
import com.smart.tailor.service.BrandService;
import com.smart.tailor.service.OrderService;
import com.smart.tailor.service.SampleProductDataService;
import com.smart.tailor.utils.request.SampleProductDataRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SampleProductDataServiceImpl implements SampleProductDataService {
    private final SampleProductDataRepository sampleRepository;
    private final BrandService brandService;
    private final OrderService orderService;

    @Override
    public SampleProductData addNewSample(SampleProductDataRequest sampleProductData) {
        try {
            if (checkValidateSample(sampleProductData)) {
                var entity = SampleProductData.builder().orderID(sampleProductData.getOrderID()).brandID(sampleProductData.getBrandID()).images(sampleProductData.getImages() != null ? sampleProductData.getImages() : null).video(sampleProductData.getVideo() != null ? sampleProductData.getVideo() : null).description(sampleProductData.getDescription() != null ? sampleProductData.getDescription() : null).build();
                var data = sampleRepository.save(entity);
                return data;
            } else {
                throw new RuntimeException(MessageConstant.MISSING_ARGUMENT);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public SampleProductData updateSample(SampleProductDataRequest sampleProductData) {
        try {
            if (checkValidateSample(sampleProductData)) {
                var entity = SampleProductData.builder().orderID(sampleProductData.getOrderID()).brandID(sampleProductData.getBrandID()).images(sampleProductData.getImages() != null ? sampleProductData.getImages() : null).video(sampleProductData.getVideo() != null ? sampleProductData.getVideo() : null).description(sampleProductData.getDescription() != null ? sampleProductData.getDescription() : null).build();
                var data = sampleRepository.save(entity);
                return data;
            } else {
                throw new RuntimeException(MessageConstant.MISSING_ARGUMENT);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public SampleProductData getByID(UUID sampleID) {
        try {
            if (sampleID == null) {
                throw new RuntimeException(MessageConstant.MISSING_ARGUMENT);
            }

            return sampleRepository.findById(sampleID).isEmpty() ? null : sampleRepository.findById(sampleID).get();

        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public List<SampleProductData> getByOrderID(UUID orderID) {
        try {
            if (orderID == null) {
                throw new RuntimeException(MessageConstant.MISSING_ARGUMENT);
            }

            return sampleRepository.findAllByOrderID(orderID);
        } catch (Exception ex) {
            throw ex;
        }
    }

    private Boolean checkValidateSample(SampleProductDataRequest data) {
        if (data == null) {
            return false;
        }
        if (data.getOrderID() == null) {
            return false;
        }
        if (data.getBrandID() == null) {
            return false;
        }

        var orderID = data.getOrderID();
        var order = orderService.getOrderById(orderID);
        if (order.isEmpty()) {
            return false;
        }
        var brandID = data.getBrandID();
        var brand = brandService.findBrandById(brandID);
        if (brand.isEmpty()) {
            return false;
        }

        String image = data.getImages() != null ? data.getImages() : null;
        String video = data.getVideo() != null ? data.getVideo() : null;
        return image != null || video != null;
    }
}
