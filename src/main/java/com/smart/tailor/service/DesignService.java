package com.smart.tailor.service;

import com.smart.tailor.entities.Design;
import com.smart.tailor.utils.request.DesignRequest;
import com.smart.tailor.utils.response.APIResponse;

import java.util.UUID;

public interface DesignService {
    APIResponse createDesign(DesignRequest designRequest);

    Design getDesignByID(UUID designID);
}
