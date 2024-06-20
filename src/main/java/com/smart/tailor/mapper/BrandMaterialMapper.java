package com.smart.tailor.mapper;

import com.smart.tailor.entities.BrandMaterial;
import com.smart.tailor.entities.BrandMaterialKey;
import com.smart.tailor.utils.response.BrandMaterialResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BrandMaterialMapper {
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