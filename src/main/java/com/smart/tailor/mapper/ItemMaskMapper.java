package com.smart.tailor.mapper;

import com.smart.tailor.entities.ItemMask;
import com.smart.tailor.utils.response.ItemMaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Base64;

@Mapper(componentModel = "spring", uses = {MaterialMapper.class})
public interface ItemMaskMapper {
    @Mapping(source = "itemMask.itemMaskID", target = "itemMaskID")
    @Mapping(source = "itemMask.indexZ", target = "indexZ")
    @Mapping(source = "itemMask.material", target = "material")
    @Mapping(target = "imageUrl", expression = "java(decodeByteArrayToString(itemMask.getImageUrl()))")
    ItemMaskResponse mapperToItemMaskResponse(ItemMask itemMask);

    default String decodeByteArrayToString(byte[] values){
        if(values != null){
            return new String(Base64.getDecoder().decode(values));
        }
        return null;
    }

}
