package com.smart.tailor.service;

import com.smart.tailor.entities.DesignDetail;
import com.smart.tailor.utils.request.DesignDetailRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.DesignDetailCustomResponse;
import com.smart.tailor.utils.response.DesignDetailResponse;
import com.smart.tailor.utils.response.OrderDetailPriceResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DesignDetailService {
    DesignDetailCustomResponse findAllByOrderID(UUID orderID);

    DesignDetailResponse findByID(UUID orderID);

    APIResponse createDesignDetail(DesignDetailRequest designDetailRequest);

    DesignDetailResponse updateDesignDetail(DesignDetail designDetail);

    DesignDetail updateDetailByID(DesignDetail designDetail);

    Optional<DesignDetail> getDesignDetailObjectByID(UUID detailID);

    DesignDetail getDetailOfOrderBaseOnBrandID(UUID orderID, UUID brandID);

    List<DesignDetail> getDesignDetailBySubOrderID(UUID subOrderID);

    OrderDetailPriceResponse calculateTotalPriceForSpecificOrder(UUID parentOrderID) throws Exception;
}
