package com.smart.tailor.service;

import com.smart.tailor.entities.Category;
import com.smart.tailor.utils.request.CategoryRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.CategoryResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryService {
    Optional<Category> findByCategoryName(String categoryName);

    APIResponse createCategory(String categoryName);

    List<CategoryResponse> findAllCatgories();

    APIResponse findCategoryByID(UUID categoryID);

    APIResponse updateCategory(CategoryRequest categoryRequest);

    Category mapperToCategory(CategoryResponse categoryResponse);
}
