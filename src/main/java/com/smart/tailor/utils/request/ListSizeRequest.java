package com.smart.tailor.utils.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListSizeRequest {
    @Valid
    List<@Valid SizeRequest> sizeRequestList;
}
