package com.smart.tailor.mapper;

import com.smart.tailor.entities.ExpertTailoring;
import com.smart.tailor.utils.response.ExpertTailoringResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpertTailoringMapper {
    @Mapping(source = "expertTailoring.createDate", target = "createDate")
    @Mapping(source = "expertTailoring.lastModifiedDate", target = "lastModifiedDate")
    ExpertTailoringResponse mapperToExpertTailoringResponse(ExpertTailoring expertTailoring);
}