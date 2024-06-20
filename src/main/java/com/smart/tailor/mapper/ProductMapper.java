package com.smart.tailor.mapper;

import com.smart.tailor.entities.Product;
import com.smart.tailor.utils.response.ProductResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse mapperToProductResponse(Product product);
}
