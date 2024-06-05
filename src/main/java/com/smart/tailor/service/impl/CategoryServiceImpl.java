package com.smart.tailor.service.impl;

import com.smart.tailor.entities.Category;
import com.smart.tailor.mapper.CategoryMapper;
import com.smart.tailor.repository.CategoryRepository;
import com.smart.tailor.service.CategoryService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private final CategoryMapper categoryMapper;

    @Override
    public Optional<Category> findByCategoryName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName.toLowerCase());
    }

    @Override
    public Category createCategory(String categoryName) {
        if(Utilities.isNonNullOrEmpty(categoryName)){
            Optional<Category> categoryOptional = findByCategoryName(categoryName.toLowerCase());
            if(categoryOptional.isEmpty()){
                return categoryRepository.save(
                        Category
                                .builder()
                                .categoryName(categoryName.toLowerCase())
                                .build()
                );
            }
        }
        return null;
    }

    @Override
    public List<CategoryResponse> findAllCatgories() {
        return categoryRepository
                .findAll()
                .stream()
                .map(categoryMapper::mapperToCategoryResponse)
                .collect(Collectors.toList());
    }
}
