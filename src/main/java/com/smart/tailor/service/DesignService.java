package com.smart.tailor.service;

import com.smart.tailor.entities.Design;
import com.smart.tailor.utils.request.DesignRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.DesignResponse;

import java.util.List;
import java.util.UUID;

public interface DesignService {
    APIResponse createDesign(DesignRequest designRequest);

    Design getDesignByID(UUID designID);

    DesignResponse getDesignResponseByID(UUID designID);

    List<DesignResponse> getAllDesignByUserID(UUID userID);

    List<DesignResponse> getAllDesign();

    APIResponse getAllDesignByUserIDAndRoleName(UUID userID, String roleName);

    APIResponse updatePublicStatusDesign(UUID designID);
}
