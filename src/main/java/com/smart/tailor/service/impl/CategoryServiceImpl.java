package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Category;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ItemAlreadyExistException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.CategoryMapper;
import com.smart.tailor.repository.CategoryRepository;
import com.smart.tailor.service.CategoryService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.CategoryRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.CategoryResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
    @Transactional
    public APIResponse createCategory(String categoryName) {
        if(!Utilities.isStringNotNullOrEmpty(categoryName)){
            throw new BadRequestException(MessageConstant.DATA_IS_EMPTY + " : categoryName");
        }

        Optional<Category> categoryOptional = findByCategoryName(categoryName.toLowerCase());
        if(categoryOptional.isPresent()) {
            throw new ItemAlreadyExistException(MessageConstant.CATEGORY_IS_EXISTED);
        }
        var category = categoryRepository.save(
                Category
                        .builder()
                        .categoryName(categoryName.toLowerCase())
                        .build()
        );

        return APIResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message(MessageConstant.ADD_NEW_CATEGORY_SUCCESSFULLY)
                .data(categoryMapper.mapperToCategoryResponse(category))
                .build();
    }

    @Override
    public List<CategoryResponse> findAllCatgories() {
        return categoryRepository
                .findAll()
                .stream()
                .map(categoryMapper::mapperToCategoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse findCategoryByID(UUID categoryID) {
        var categoryOptional = categoryRepository.findByCategoryID(categoryID);
        if(categoryOptional.isPresent()){
            return categoryMapper.mapperToCategoryResponse(categoryOptional.get());
        }
        return null;
    }

    @Override
    @Transactional
    public APIResponse updateCategory(CategoryRequest categoryRequest) {
        // Check Category ID is Existed or not
        var categoryResponse = categoryRepository.findByCategoryID(UUID.fromString(categoryRequest.getCategoryID()))
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_CATEGORY));

        // Check Category Name is Existed or not
        var categoryNameExisted = findByCategoryName(categoryRequest.getCategoryName());

        if(categoryNameExisted.isPresent()){
            if(!categoryNameExisted.get().getCategoryID().toString().equals((categoryResponse.getCategoryID().toString()))){
                throw new ItemAlreadyExistException(MessageConstant.CATEGORY_IS_EXISTED);
            }
        }

        // Update Category when CategoryID is Existed and CategoryName is not Existed
        var updateCategory = categoryRepository.save(
                Category
                    .builder()
                    .categoryID(categoryResponse.getCategoryID())
                    .categoryName(categoryRequest.getCategoryName())
                    .build()
        );

        return APIResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message(MessageConstant.UPDATE_CATEGORY_SUCCESSFULLY)
                .data(categoryMapper.mapperToCategoryResponse(updateCategory))
                .build();
    }

    @Override
    public Category mapperToCategory(CategoryResponse categoryResponse) {
        return categoryMapper.mapperToCategory(categoryResponse);
    }
}
