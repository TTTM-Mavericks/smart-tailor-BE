package com.smart.tailor.mapper;

import com.smart.tailor.entities.Brand;
import com.smart.tailor.utils.response.BrandResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    //    BrandResponse
    BrandResponse mapperToBrandResponse(Brand brand);
}
