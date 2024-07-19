package com.smart.tailor.mapper;

import com.smart.tailor.entities.PayOSData;
import com.smart.tailor.utils.response.PayOSResponseData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PayOSDataMapper {
    PayOSResponseData mapToPayOSResponseData(PayOSData payOSData);
}
