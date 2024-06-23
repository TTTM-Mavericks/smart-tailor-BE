package com.smart.tailor.mapper;

import com.smart.tailor.entities.PartOfDesign;
import com.smart.tailor.utils.response.PartOfDesignResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ItemMaskMapper.class})
public interface PartOfDesignMapper {
    @Mapping(source = "partOfDesign.partOfDesignID", target = "partOfDesignID")
    @Mapping(source = "partOfDesign.itemMaskList", target = "itemMaskList")
    PartOfDesignResponse mapperToPartOfDesignResponse(PartOfDesign partOfDesign);

    List<PartOfDesignResponse> mapperToListPartOfDesignResponse(List<PartOfDesign> partOfDesign);
}
