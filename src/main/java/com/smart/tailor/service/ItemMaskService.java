package com.smart.tailor.service;

import com.smart.tailor.entities.ItemMask;
import com.smart.tailor.entities.PartOfDesign;
import com.smart.tailor.utils.request.ItemMaskRequest;
import com.smart.tailor.utils.response.ItemMaskResponse;

import java.util.List;


public interface ItemMaskService {
    List<ItemMask> createItemMask(PartOfDesign partOfDesign, List<ItemMaskRequest> itemMaskRequestList);

    List<ItemMaskResponse> getListItemMaskByPartOfDesignID(String partOfDesignID);

    ItemMaskResponse getItemMaskByItemMaskID(String itemMaskID);

    List<ItemMaskResponse> getAllItemMask();

    void deleteItemMaskByPartOfDesignID(String partOfDesignID);

    void deleteItemMaskByDesignID(String designID);
}
