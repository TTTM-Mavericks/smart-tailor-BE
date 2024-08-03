package com.smart.tailor.utils.request;

import com.smart.tailor.validate.ValidStringUUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequest {
    @NotBlank(message = "Category ID is required")
    @ValidStringUUID(message = "Category ID must be a valid UUID")
    private String categoryID;

    @NotBlank(message = "Category Name is required")
    @Size(max = 50, message = "Category Name must not exceed 50 characters")
    private String categoryName;
}
