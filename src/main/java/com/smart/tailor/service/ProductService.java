package com.smart.tailor.service;

import com.smart.tailor.config.CustomExeption;
import com.smart.tailor.entities.Product;
import com.smart.tailor.utils.request.ProductRequest;
import com.smart.tailor.utils.response.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    void saveProduct(ProductRequest productRequest) throws Exception;

    ProductResponse getProduct(UUID productID) throws CustomExeption;

    List<Product> findAllProductByBrandName(String brandName) throws CustomExeption;

    List<Product> findAllProductByBrandID(UUID brandID) throws CustomExeption;

    List<Product> findAllProductByDesignID(UUID designID) throws CustomExeption;

    List<Product> findAllProductByUserID(UUID userID) throws CustomExeption;
}
