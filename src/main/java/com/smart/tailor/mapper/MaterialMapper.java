package com.smart.tailor.mapper;

import com.smart.tailor.entities.Material;
import com.smart.tailor.utils.response.MaterialResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MaterialMapper {
    @Mapping(source = "category.categoryName", target = "categoryName")
    MaterialResponse mapperToMaterialResponse(Material material);
}
