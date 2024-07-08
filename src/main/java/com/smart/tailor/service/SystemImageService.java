package com.smart.tailor.service;

import com.smart.tailor.utils.request.SystemImageRequest;
import com.smart.tailor.utils.response.SystemImageResponse;

import java.util.List;
import java.util.UUID;

public interface SystemImageService {
    SystemImageResponse addNewSystemImage(SystemImageRequest systemImageRequest);

    List<SystemImageResponse> getAllSystemImage();

    List<SystemImageResponse> getAllSystemImageByImageType(String imageType);

    List<SystemImageResponse> getAllPremiumSystemImage();

    List<SystemImageResponse> getSystemImageByTypeAndIsPremium(String imageType, Boolean isPremium);

    SystemImageResponse getSystemImageById(UUID systemImageId);
}
