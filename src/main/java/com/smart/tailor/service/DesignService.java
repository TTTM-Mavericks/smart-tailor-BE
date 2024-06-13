package com.smart.tailor.service;

import com.smart.tailor.utils.request.DesignRequest;
import com.smart.tailor.utils.response.APIResponse;

public interface DesignService {
    APIResponse createDesign(DesignRequest designRequest);
}
