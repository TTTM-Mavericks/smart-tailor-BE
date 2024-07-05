package com.smart.tailor.utils.request;

import com.smart.tailor.validate.ValidStringUUID;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateDesignRequest {
    @NotBlank(message = "designID can not be blank")
    @NotNull(message = "designID can not be null")
    @ValidStringUUID(message = "designID is invalid of type UUID")
    private String designID;

    @NotEmpty(message = "partOfDesign is not empty")
    @Valid
    private List<@Valid PartOfDesignRequest> partOfDesign;
}
