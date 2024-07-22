package com.smart.tailor.utils.response;

import com.smart.tailor.utils.request.PayOSItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayOSCreationResponse {
    private String code;
    private String desc;
    private PayOSCreationResponseData data;
    private String signature;
}
