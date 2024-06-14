package com.smart.tailor.service;

import com.smart.tailor.utils.request.PartOfDesignRequest;
import com.smart.tailor.utils.response.APIResponse;

import java.util.List;

public interface PartOfDesignService {
    APIResponse createPartOfDesign(List<PartOfDesignRequest> partOfDesignRequestList);
}
