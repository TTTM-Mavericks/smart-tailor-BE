package com.smart.tailor.utils.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandLaborQuantityListRequest {
    @Valid
    private List<@Valid BrandLaborQuantityRequest> brandLaborQuantity;
}
