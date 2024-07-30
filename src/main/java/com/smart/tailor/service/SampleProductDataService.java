package com.smart.tailor.service;

import com.smart.tailor.entities.SampleProductData;
import com.smart.tailor.utils.request.SampleProductDataRequest;

import java.util.UUID;

public interface SampleProductDataService {
    SampleProductData addNewSample(SampleProductDataRequest sampleProductData);

    SampleProductData updateSample(SampleProductDataRequest sampleProductData);

    SampleProductData getByID(UUID sampleID);
}
