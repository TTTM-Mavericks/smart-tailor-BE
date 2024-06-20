package com.smart.tailor.mapper;

import com.smart.tailor.entities.Design;
import com.smart.tailor.utils.response.DesignResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface DesignMapper {
    @Mapping(source = "design.designID", target = "designID")
    @Mapping(source = "user", target = "userResponse")
    DesignResponse mapperToDesignResponse(Design design);
}
