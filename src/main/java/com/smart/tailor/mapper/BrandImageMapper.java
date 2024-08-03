package com.smart.tailor.mapper;

import com.smart.tailor.entities.BrandImage;
import com.smart.tailor.utils.response.BrandImageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Base64;

@Mapper(componentModel = "spring")
public interface BrandImageMapper {

    @Mapping(target = "imageUrl", expression = "java(decodeByteArrayToString(brandImage.getImageUrl()))")
    BrandImageResponse mapToBrandImageResponse(BrandImage brandImage);

    default String decodeByteArrayToString(byte[] values) {
        if (values != null) {
            return new String(Base64.getDecoder().decode(values));
        }
        return null;
    }
}
