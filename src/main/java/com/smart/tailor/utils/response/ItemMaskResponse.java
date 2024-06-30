package com.smart.tailor.utils.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.smart.tailor.enums.PrintType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemMaskResponse {
    private UUID itemMaskID;

    private String itemMaskName;

    private String typeOfItem;

    private MaterialResponse material;

    private Boolean isSystemItem;

    private Float positionX;

    private Float positionY;

    private Float scaleX;

    private Float scaleY;

    private Integer indexZ;

    private String imageUrl;

    private PrintType printType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedDate;
}
