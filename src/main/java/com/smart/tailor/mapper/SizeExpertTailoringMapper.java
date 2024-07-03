package com.smart.tailor.mapper;

import com.smart.tailor.entities.SizeExpertTailoring;
import com.smart.tailor.utils.response.SizeExpertTailoringResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SizeExpertTailoringMapper {
    @Mapping(source = "sizeExpertTailoring.expertTailoring.expertTailoringName", target = "expertTailoringName")
    @Mapping(source = "sizeExpertTailoring.size.sizeName", target = "sizeName")
    @Mapping(source = "sizeExpertTailoring.sizeExpertTailoringKey.expertTailoringID", target = "expertTailoringID")
    @Mapping(source = "sizeExpertTailoring.sizeExpertTailoringKey.sizeID", target = "sizeID")
    @Mapping(source = "sizeExpertTailoring.unit", target = "unit")
    @Mapping(source = "sizeExpertTailoring.createDate", target = "createDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "sizeExpertTailoring.lastModifiedDate", target = "lastModifiedDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    SizeExpertTailoringResponse mapperToSizeExpertTailoringResponse(SizeExpertTailoring sizeExpertTailoring);
}
