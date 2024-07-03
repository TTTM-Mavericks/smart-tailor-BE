package com.smart.tailor.mapper;

import com.smart.tailor.entities.DesignDetail;
import com.smart.tailor.utils.response.DesignDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DesignMapper.class, OrderMapper.class, BrandMapper.class})
public interface DesignDetailMapper {
    @Mapping(source = "designDetail.designDetailID", target = "designDetailId")
    @Mapping(source = "designDetail.design", target = "design")
    @Mapping(source = "designDetail.order", target = "order")
    @Mapping(source = "designDetail.brand", target = "brand")
    @Mapping(source = "designDetail.quantity", target = "quantity")
    @Mapping(source = "designDetail.size", target = "size")
    @Mapping(source = "designDetail.detailStatus", target = "detailStatus")
    DesignDetailResponse mapperToDesignDetailResponse(DesignDetail designDetail);
}
