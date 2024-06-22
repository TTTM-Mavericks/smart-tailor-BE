package com.smart.tailor.service;

import com.smart.tailor.entities.Design;
import com.smart.tailor.utils.request.PartOfDesignRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.PartOfDesignResponse;

import java.util.List;
import java.util.UUID;

public interface PartOfDesignService {
    APIResponse createPartOfDesign(Design design, List<PartOfDesignRequest> partOfDesignRequestList);

    List<PartOfDesignResponse> getListPartOfDesignByDesignID(UUID designID);

    PartOfDesignResponse getPartOfDesignByPartOfDesignID(UUID partOfDesignID);

    List<PartOfDesignResponse> getAllPartOfDesign();
}
