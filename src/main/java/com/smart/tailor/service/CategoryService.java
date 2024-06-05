package com.smart.tailor.service;

import com.smart.tailor.entities.Category;
import com.smart.tailor.utils.response.CategoryResponse;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Optional<Category> findByCategoryName(String categoryName);

    Category createCategory(String categoryName);

    List<CategoryResponse> findAllCatgories();
}
