package com.smart.tailor.mapper;

import com.smart.tailor.entities.Category;
import com.smart.tailor.utils.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse mapperToCategoryResponse(Category category);

    Category mapperToCategory(CategoryResponse categoryResponse);
}
