package com.smart.tailor.utils.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.smart.tailor.utils.request.PartOfDesignRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DesignResponse {
    private UUID designID;

    private UserResponse userResponse;

    private String expertTailoringName;

    private String titleDesign;

    private Boolean publicStatus;

    private String imageUrl;

    private String color;

    private List<PartOfDesignResponse> partOfDesignList;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedDate;
}
