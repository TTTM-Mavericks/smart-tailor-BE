package com.smart.tailor.service;

import com.smart.tailor.entities.Design;
import com.smart.tailor.utils.request.CloneDesignRequest;
import com.smart.tailor.utils.request.DesignRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.DesignResponse;

import java.util.List;


public interface DesignService {
    APIResponse addNewDesign(DesignRequest designRequest);

    Design getDesignByID(String designID);

    DesignResponse getDesignByOrderID(String orderID);

    Design getDesignObjectByOrderID(String orderID);

    DesignResponse getDesignResponseByID(String designID);

    List<DesignResponse> getAllDesignByUserID(String userID);

    List<DesignResponse> getAllDesign();

    APIResponse getAllDesignByUserIDAndRoleName(String userID, String roleName);

    void updatePublicStatusDesign(String designID);

    void addNewCloneDesignFromBrandDesign(CloneDesignRequest cloneDesignRequest);

    APIResponse updateDesign(String designID, DesignRequest designRequest);
}
