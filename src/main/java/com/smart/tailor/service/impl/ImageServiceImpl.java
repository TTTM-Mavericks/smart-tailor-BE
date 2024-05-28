package com.smart.tailor.service.impl;

import com.smart.tailor.entities.Image;
import com.smart.tailor.repository.ImageRepository;
import com.smart.tailor.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    @Override
    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public String getImageUrl(UUID imageID) {
        Optional<Image> image = imageRepository.getByImageID(imageID);
        if (image.isPresent()) {
            return image.get().getImageUrl();
        }
        return null;
    }
}
