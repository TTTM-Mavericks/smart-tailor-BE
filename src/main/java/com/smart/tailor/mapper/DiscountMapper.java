package com.smart.tailor.mapper;

import com.smart.tailor.entities.Discount;
import com.smart.tailor.utils.response.DiscountResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DiscountMapper {
    DiscountResponse mapToDiscountResponse(Discount discount);
}
