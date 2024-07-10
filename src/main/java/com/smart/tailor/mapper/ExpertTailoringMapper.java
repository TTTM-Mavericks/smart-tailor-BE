package com.smart.tailor.mapper;

import com.smart.tailor.entities.ExpertTailoring;
import com.smart.tailor.utils.response.ExpertTailoringResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpertTailoringMapper {
    @Mapping(source = "expertTailoring.expertTailoringID", target = "expertTailoringID")
    @Mapping(source = "expertTailoring.status", target = "status")
    ExpertTailoringResponse mapperToExpertTailoringResponse(ExpertTailoring expertTailoring);
}
