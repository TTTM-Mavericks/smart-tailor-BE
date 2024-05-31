package com.smart.tailor.utils.response;

import com.smart.tailor.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponse {
    private UUID brandID;
    private User user;
    private String brandName;
    private String bankName;
}
