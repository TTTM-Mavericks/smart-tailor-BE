package com.smart.tailor.mapper;

import com.smart.tailor.entities.ItemMask;
import com.smart.tailor.utils.response.ItemMaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMaskMapper {
    @Mapping(source = "itemMask.itemMaskID", target = "itemMaskID")
    @Mapping(source = "itemMask.indexZ", target = "indexZ")
    ItemMaskResponse mapperToItemMaskResponse(ItemMask itemMask);

    List<ItemMaskResponse> mapperToListItemMaskResponse(List<ItemMask> itemMaskList);
}
