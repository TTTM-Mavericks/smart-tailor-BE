package com.smart.tailor.service;

import com.smart.tailor.entities.Design;
import com.smart.tailor.utils.request.CloneDesignRequest;
import com.smart.tailor.utils.request.DesignRequest;
import com.smart.tailor.utils.request.PartOfDesignRequest;
import com.smart.tailor.utils.request.UpdateDesignRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.DesignResponse;

import java.util.List;
import java.util.UUID;

public interface DesignService {
    void addNewDesign(DesignRequest designRequest);

    Design getDesignByID(UUID designID);

    DesignResponse getDesignResponseByID(UUID designID);

    List<DesignResponse> getAllDesignByUserID(UUID userID);

    List<DesignResponse> getAllDesign();

    APIResponse getAllDesignByUserIDAndRoleName(UUID userID, String roleName);

    void updatePublicStatusDesign(UUID designID);

    void addNewCloneDesignFromBrandDesign(CloneDesignRequest cloneDesignRequest);

    void updateDesign(UpdateDesignRequest updateDesignRequest);
}
