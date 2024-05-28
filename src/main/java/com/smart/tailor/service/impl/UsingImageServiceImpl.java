package com.smart.tailor.service.impl;

import com.smart.tailor.entities.UsingImage;
import com.smart.tailor.repository.UsingImageRepository;
import com.smart.tailor.service.UsingImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsingImageServiceImpl implements UsingImageService {
    private final UsingImageRepository usingImageRepository;

    @Override
    public UsingImage saveUsingImage(UsingImage usingImage) {
        return usingImageRepository.save(usingImage);
    }

    @Override
    public UUID getImage(String type, UUID relationID) {
        Optional<UsingImage> usingImage = usingImageRepository.getUsingImageByTypeAndRelationID(type, relationID);
        if (usingImage.isPresent()) {
            return usingImage.get().getImage().getImageID();
        }
        return null;
    }

}
