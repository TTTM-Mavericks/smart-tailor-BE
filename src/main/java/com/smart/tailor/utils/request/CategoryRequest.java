package com.smart.tailor.utils.request;

import com.smart.tailor.validate.ValidStringUUID;
import com.smart.tailor.validate.ValidUUID;
import jakarta.validation.constraints.*;
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
    @NotNull(message = "Category ID must not be null")
    @NotBlank(message = "Category ID must not be blank")
    @ValidStringUUID(message = "Category ID must be a valid UUID")
    private String categoryID;

    @NotNull(message = "Category Name must not be null")
    @NotBlank(message = "Category Name must not be blank")
    @Size(max = 50, message = "Category Name must not exceed 50 characters")
    private String categoryName;
}
