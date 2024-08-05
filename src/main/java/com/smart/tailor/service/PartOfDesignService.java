package com.smart.tailor.service;

import com.smart.tailor.entities.Design;
import com.smart.tailor.entities.PartOfDesign;
import com.smart.tailor.utils.request.PartOfDesignRequest;
import com.smart.tailor.utils.response.PartOfDesignResponse;

import java.util.List;


public interface PartOfDesignService {
    List<PartOfDesign> createPartOfDesign(Design design, List<PartOfDesignRequest> partOfDesignRequestList);

    List<PartOfDesignResponse> getListPartOfDesignByDesignID(String designID);

    PartOfDesignResponse getPartOfDesignByPartOfDesignID(String partOfDesignID);

    List<PartOfDesignResponse> getAllPartOfDesign();

    void deletePartOfDesignByDesignID(String designID);
}
