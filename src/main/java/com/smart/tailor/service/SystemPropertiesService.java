package com.smart.tailor.service;

import com.smart.tailor.entities.SystemProperties;
import com.smart.tailor.utils.request.SystemPropertiesRequest;
import com.smart.tailor.utils.response.SystemPropertiesResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SystemPropertiesService {
    SystemPropertiesResponse addNewSystemProperty(SystemPropertiesRequest systemProperties);

    List<SystemPropertiesResponse> getAllByPropertyType(String propertyType);

    SystemPropertiesResponse getByID(UUID propertyID);

    Optional<SystemProperties> getObjectByID(UUID propertyID);

    List<SystemPropertiesResponse> getAll();
}
