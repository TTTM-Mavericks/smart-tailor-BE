package com.smart.tailor.mapper;

import com.smart.tailor.entities.Design;
import com.smart.tailor.utils.response.DesignResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Base64;


@Mapper(componentModel = "spring", uses = {UserMapper.class, PartOfDesignMapper.class, ExpertTailoringMapper.class})
public interface DesignMapper {
    @Mapping(source = "design.designID", target = "designID")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "expertTailoring", target = "expertTailoring")
    @Mapping(target = "imageUrl", expression = "java(decodeByteArrayToString(design.getImageUrl()))")
    @Mapping(source = "design.partOfDesignList", target = "partOfDesign")
    DesignResponse mapperToDesignResponse(Design design);

    default String decodeByteArrayToString(byte[] values){
        if(values != null){
            return new String(Base64.getDecoder().decode(values));
        }
        return null;
    }
}
