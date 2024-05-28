package com.smart.tailor.service;

import com.smart.tailor.entities.Image;

import java.util.UUID;

public interface ImageService {
    Image saveImage(Image image);
    String getImageUrl(UUID imageID);
}
