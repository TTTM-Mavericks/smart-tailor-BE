package com.smart.tailor.utils.request;

import com.smart.tailor.validate.ValidStringUUID;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
public class CloneDesignRequest {
    @NotBlank(message = "userID can not be blank")
    @NotNull(message = "userID can not be null")
    @ValidStringUUID(message = "userID is invalid of type UUID")
    private String userID;

    @NotBlank(message = "brandDesignID can not be blank")
    @NotNull(message = "brandDesignID can not be null")
    @ValidStringUUID(message = "brandDesignID is invalid of type UUID")
    private String brandDesignID;

    @Valid
    private List<@Valid PartOfDesignRequest> partOfDesign;
}
