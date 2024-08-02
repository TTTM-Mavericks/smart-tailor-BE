package com.smart.tailor.service;

import com.smart.tailor.utils.request.SampleProductDataRequest;
import com.smart.tailor.utils.response.SampleProductDataResponse;

import java.util.List;
import java.util.UUID;

public interface SampleProductDataService {
    void addNewSampleProductData(SampleProductDataRequest sampleProductDataRequest);

    void updateSampleProductData(UUID sampleModelID,SampleProductDataRequest sampleProductDataRequest);

    SampleProductDataResponse getSampleProductDataByID(UUID sampleModelID);

    List<SampleProductDataResponse> getSampleProductDataByParentOrderID(UUID parentOrderID);

    List<SampleProductDataResponse> getSampleProductDataByParentOrderIDAndStageID(UUID orderID, UUID stageID);
}
