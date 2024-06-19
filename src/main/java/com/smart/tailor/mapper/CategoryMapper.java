package com.smart.tailor.mapper;

import com.smart.tailor.entities.Category;
import com.smart.tailor.utils.response.CategoryResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse mapperToCategoryResponse(Category category);
}
