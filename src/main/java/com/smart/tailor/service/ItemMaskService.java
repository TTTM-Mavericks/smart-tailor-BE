package com.smart.tailor.service;

import com.smart.tailor.entities.PartOfDesign;
import com.smart.tailor.utils.request.ItemMaskRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.ItemMaskResponse;
import com.smart.tailor.utils.response.PartOfDesignResponse;

import java.util.List;
import java.util.UUID;

public interface ItemMaskService {
    APIResponse createItemMask(PartOfDesign partOfDesign, List<ItemMaskRequest> itemMaskRequestList);

    List<ItemMaskResponse> getListItemMaskByPartOfDesignID(UUID partOfDesignID);

    ItemMaskResponse getItemMaskByItemMaskID(UUID itemMaskID);

    List<ItemMaskResponse> getAllItemMask();

}
