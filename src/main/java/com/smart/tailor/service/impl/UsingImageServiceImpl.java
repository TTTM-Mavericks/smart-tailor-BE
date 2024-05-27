package com.smart.tailor.service.impl;

import com.smart.tailor.entities.UsingImage;
import com.smart.tailor.repository.UsingImageRepository;
import com.smart.tailor.service.UsingImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsingImageServiceImpl implements UsingImageService {
    private final UsingImageRepository usingImageRepository;

    @Override
    public UsingImage saveUsingImage(UsingImage usingImage) {
        return usingImageRepository.save(usingImage);
    }
}
