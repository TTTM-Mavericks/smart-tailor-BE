package com.smart.tailor.service.impl;

import com.smart.tailor.config.CustomExeption;
import com.smart.tailor.constant.ErrorConstant;
import com.smart.tailor.entities.Brand;
import com.smart.tailor.entities.Design;
import com.smart.tailor.entities.Product;
import com.smart.tailor.mapper.ProductMapper;
import com.smart.tailor.repository.ProductRepository;
import com.smart.tailor.service.BrandService;
import com.smart.tailor.service.DesignService;
import com.smart.tailor.service.ProductService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.ProductRequest;
import com.smart.tailor.utils.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final DesignService designService;
    private final BrandService brandService;
    private final ProductMapper productMapper;
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public void saveProduct(ProductRequest productRequest) throws Exception {
        try {
            if (productRequest == null) {
                throw new CustomExeption(ErrorConstant.INTERNAL_SERVER_ERROR);
            }

            if (!Utilities.isNonNullOrEmpty(productRequest.getProductName())) {
                throw new CustomExeption(ErrorConstant.INTERNAL_SERVER_ERROR);
            }

            if (!Utilities.isValidDouble(productRequest.getPricePerProduct().toString())) {
                throw new CustomExeption(ErrorConstant.INTERNAL_SERVER_ERROR);
            }

            if (!Utilities.isValidBoolean(productRequest.getGender())) {
                throw new CustomExeption(ErrorConstant.INTERNAL_SERVER_ERROR);
            }

            if (!Utilities.isValidNumber(productRequest.getRating().toString())) {
                throw new CustomExeption(ErrorConstant.INTERNAL_SERVER_ERROR);
            }

            if (!Utilities.isValidBoolean(productRequest.getPublicStatus())) {
                throw new CustomExeption(ErrorConstant.INTERNAL_SERVER_ERROR);
            }

            if (!Utilities.isNonNullOrEmpty(productRequest.getSize())) {
                throw new CustomExeption(ErrorConstant.INTERNAL_SERVER_ERROR);
            }

            Design design = designService.getDesignByID(productRequest.getDesignID());
            Brand brand = brandService.getBrandById(productRequest.getBrandID()).isPresent() ? brandService.getBrandById(productRequest.getBrandID()).get() : null;
            Product product = Product.builder()
                    .design(design != null ? design : new Design())
                    .brand(brand != null ? brand : new Brand())
                    .productName(productRequest.getProductName())
                    .pricePerProduct(productRequest.getPricePerProduct())
                    .gender(productRequest.getGender())
                    .rating(productRequest.getRating())
                    .description(productRequest.getDescription())
                    .publicStatus(productRequest.getPublicStatus())
                    .size(productRequest.getSize())
                    .productStatus(productRequest.getProductStatus())
                    .build();
            productRepository.save(product);
        } catch (Exception ex) {
            logger.error("ERROR IN PRODUCT SERVICE - FIND PRODUCT BY BRAND NAME: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public ProductResponse getProduct(UUID productID) throws CustomExeption {
        try {
            if (productID == null) {
                throw new CustomExeption(ErrorConstant.INTERNAL_SERVER_ERROR);
            }
            ProductResponse productResponse = productMapper.mapperToProductResponse(productRepository.getReferenceById(productID));
            return productResponse;
        } catch (Exception ex) {
            logger.error("ERROR IN PRODUCT SERVICE - FIND PRODUCT BY BRAND NAME: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<Product> findAllProductByBrandName(String brandName) throws CustomExeption {
        try {
            if (!Utilities.isNonNullOrEmpty(brandName)) {
                throw new CustomExeption(ErrorConstant.INTERNAL_SERVER_ERROR);
            }
            List<Product> productList = productRepository.findAllProductByBrandName(brandName);
            List<ProductResponse> responseList = null;
            for (Product p : productList) {
                if (responseList == null) {
                    responseList = new ArrayList<>();
                }
                if (p != null) {
                    responseList.add(productMapper.mapperToProductResponse(p));
                }
            }
            return productList;
        } catch (Exception ex) {
            logger.error("ERROR IN PRODUCT SERVICE - FIND PRODUCT BY BRAND NAME: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<Product> findAllProductByBrandID(UUID brandID) throws CustomExeption {
        try {
            if (brandID == null) {
                throw new CustomExeption(ErrorConstant.INTERNAL_SERVER_ERROR);
            }
            List<Product> productList = productRepository.findAllProductByBrandID(brandID);
            List<ProductResponse> responseList = null;
            for (Product p : productList) {
                if (responseList == null) {
                    responseList = new ArrayList<>();
                }
                if (p != null) {
                    responseList.add(productMapper.mapperToProductResponse(p));
                }
            }
            return productList;
        } catch (Exception ex) {
            logger.error("ERROR IN PRODUCT SERVICE - FIND PRODUCT BY BRAND ID: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<Product> findAllProductByDesignID(UUID designID) throws CustomExeption {
        try {
            if (designID == null) {
                throw new CustomExeption(ErrorConstant.INTERNAL_SERVER_ERROR);
            }
            List<Product> productList = productRepository.findAllProductByDesignID(designID);
            List<ProductResponse> responseList = null;
            for (Product p : productList) {
                if (responseList == null) {
                    responseList = new ArrayList<>();
                }
                if (p != null) {
                    responseList.add(productMapper.mapperToProductResponse(p));
                }
            }
            return productList;
        } catch (Exception ex) {
            logger.error("ERROR IN PRODUCT SERVICE - FIND PRODUCT BY DESIGN ID: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<Product> findAllProductByUserID(UUID userID) throws CustomExeption {
        try {
            if (userID == null) {
                throw new CustomExeption(ErrorConstant.INTERNAL_SERVER_ERROR);
            }
            List<Product> productList = productRepository.findAllProductByUserID(userID);
            List<ProductResponse> responseList = null;
            for (Product p : productList) {
                if (responseList == null) {
                    responseList = new ArrayList<>();
                }
                if (p != null) {
                    responseList.add(productMapper.mapperToProductResponse(p));
                }
            }
            return productList;
        } catch (Exception ex) {
            logger.error("ERROR IN PRODUCT SERVICE - FIND PRODUCT BY USER ID: {}", ex.getMessage());
            throw ex;
        }
    }
}
