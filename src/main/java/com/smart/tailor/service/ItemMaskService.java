package com.smart.tailor.service;

import com.smart.tailor.utils.request.ItemMaskRequest;
import com.smart.tailor.utils.response.APIResponse;

import java.util.List;

public interface ItemMaskService {
    APIResponse createItemMask(List<ItemMaskRequest> itemMaskRequestList);
}
