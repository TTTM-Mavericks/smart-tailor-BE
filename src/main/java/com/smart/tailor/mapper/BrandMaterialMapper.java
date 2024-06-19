package com.smart.tailor.mapper;

import com.smart.tailor.entities.BrandMaterial;
import com.smart.tailor.entities.BrandMaterialKey;
import com.smart.tailor.utils.response.BrandMaterialResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BrandMaterialMapper {
//    default String mapBrandMaterialKeyToBrandName(BrandMaterialKey brandMaterialKey) {
//        return brandMaterialKey.getBrand().getBrandName();
//    }
//
//    default String mapBrandMaterialKeyToMaterialName(BrandMaterialKey brandMaterialKey) {
//        return brandMaterialKey.getMaterial().getMaterialName();
//    }
//
//    default String mapBrandMaterialKeyToCategoryName(BrandMaterialKey brandMaterialKey) {
//        return brandMaterialKey.getMaterial().getCategory().getCategoryName();
//    }
//
//    @Mapping(target = "brandName", expression = "java(mapBrandMaterialKeyToBrandName(brandMaterial.getBrandMaterialKey()))")
//    @Mapping(target = "materialName", expression = "java(mapBrandMaterialKeyToMaterialName(brandMaterial.getBrandMaterialKey()))")
//    @Mapping(target = "categoryName", expression = "java(mapBrandMaterialKeyToCategoryName(brandMaterial.getBrandMaterialKey()))")
    @Mapping(source = "brandMaterial.brandMaterialKey.material.materialName", target = "materialName")
    @Mapping(source = "brandMaterial.brandMaterialKey.material.category.categoryName", target = "categoryName")
    @Mapping(source = "brandMaterial.brandMaterialKey.brand.brandName", target = "brandName")
    @Mapping(source = "brandMaterial.brandMaterialKey.material.hsCode", target = "hsCode")
    @Mapping(source = "brandMaterial.brandMaterialKey.material.unit", target = "unit")
    @Mapping(source = "brandMaterial.brandMaterialKey.material.basePrice", target = "basePrice")
    @Mapping(source = "brandMaterial.brandPrice", target = "brandPrice")
    @Mapping(source = "brandMaterial.createDate", target = "createDate")
    @Mapping(source = "brandMaterial.lastModifiedDate", target = "lastModifiedDate")
    BrandMaterialResponse mapperToBrandMaterialResponse(BrandMaterial brandMaterial);
}