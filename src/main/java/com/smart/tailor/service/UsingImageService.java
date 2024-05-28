package com.smart.tailor.service;

import com.smart.tailor.entities.Image;
import com.smart.tailor.entities.UsingImage;

import java.util.UUID;

public interface UsingImageService {
    UsingImage saveUsingImage(UsingImage usingImage);
    UUID getImage(String type, UUID relationID);
}
