package com.smart.tailor.service;

import com.smart.tailor.entities.DesignDetail;
import com.smart.tailor.utils.request.DesignDetailRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.DesignDetailCustomResponse;
import com.smart.tailor.utils.response.DesignDetailResponse;

import java.util.List;
import java.util.UUID;

public interface DesignDetailService {
    DesignDetailCustomResponse findAllByOrderID(UUID orderID);

    APIResponse createDesignDetail(DesignDetailRequest designDetailRequest);

    DesignDetailResponse getDesignDetailByDesignAndSize(UUID designID, UUID size);

    DesignDetailResponse updateDesignDetail(DesignDetail designDetail);
}
