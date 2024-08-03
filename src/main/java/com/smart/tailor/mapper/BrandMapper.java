package com.smart.tailor.mapper;

import com.smart.tailor.entities.Brand;
import com.smart.tailor.utils.response.BrandResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BrandImageMapper.class})
public interface BrandMapper {
    @Mapping(target = "images", source = "brandImages")
    BrandResponse mapperToBrandResponse(Brand brand);
}
