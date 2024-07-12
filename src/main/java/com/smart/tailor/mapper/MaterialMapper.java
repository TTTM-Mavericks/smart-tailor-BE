package com.smart.tailor.mapper;

import com.smart.tailor.entities.Material;
import com.smart.tailor.utils.response.MaterialResponse;
import com.smart.tailor.utils.response.MaterialWithPriceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MaterialMapper {
    @Mapping(source = "material.category.categoryName", target = "categoryName")
    @Mapping(source = "material.createDate", target = "createDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "material.lastModifiedDate", target = "lastModifiedDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    MaterialResponse mapperToMaterialResponseWithoutPrices(Material material);

    @Mapping(source = "material.category.categoryName", target = "categoryName")
    @Mapping(source = "material.createDate", target = "createDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "material.lastModifiedDate", target = "lastModifiedDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    MaterialWithPriceResponse mapperToMaterialResponseWithPrices(Material material);

    default MaterialWithPriceResponse mapperToMaterialResponseWithPrices(Material material, Double minPrice, Double maxPrice) {
        MaterialWithPriceResponse response = mapperToMaterialResponseWithPrices(material);
        response.setMinPrice(minPrice);
        response.setMaxPrice(maxPrice);
        return response;
    }
}
