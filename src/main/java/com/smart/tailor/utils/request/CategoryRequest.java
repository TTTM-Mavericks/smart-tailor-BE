package com.smart.tailor.utils.request;

import com.smart.tailor.validate.ValidStringUUID;
import com.smart.tailor.validate.ValidUUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequest {
    @ValidStringUUID(message = "categoryID is invalid type of UUID")
    private String categoryID;

    @NotBlank(message = "categoryName is not blank")
    @NotEmpty(message = "categoryName is not empty")
    private String categoryName;
}
