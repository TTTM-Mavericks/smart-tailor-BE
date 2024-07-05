package com.smart.tailor.service;

import com.smart.tailor.entities.Size;
import com.smart.tailor.utils.request.ListSizeRequest;
import com.smart.tailor.utils.request.SizeRequest;
import com.smart.tailor.utils.response.SizeResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SizeService {
    void createSize(ListSizeRequest listSizeRequest);

    List<SizeResponse> findAllSizeResponse();

    void updateSize(UUID sizeID, SizeRequest sizeRequest);

    Optional<Size> findBySizeName(String sizeName);

    Optional<Size> findByID(UUID sizeID);
}
