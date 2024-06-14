package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.repository.ItemMaskRepository;
import com.smart.tailor.service.ItemMaskService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.ItemMaskRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.ItemMaskResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemMaskServiceImpl implements ItemMaskService {
    private final ItemMaskRepository itemMaskRepository;
    private final Logger logger = LoggerFactory.getLogger(ItemMaskServiceImpl.class);

    @Override
    public APIResponse createItemMask(List<ItemMaskRequest> itemMaskRequestList) {
        for(ItemMaskRequest itemMaskRequest : itemMaskRequestList){
            if(!Utilities.isValidBoolean(itemMaskRequest.getIsSystemItem())){
                return APIResponse
                        .builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(MessageConstant.INVALID_DATA_TYPE + " , isSystemItem = " + itemMaskRequest.getIsSystemItem())
                        .data(null)
                        .build();
            }

            if(!Utilities.isValidBoolean(itemMaskRequest.getIsPremium())){
                return APIResponse
                        .builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(MessageConstant.INVALID_DATA_TYPE + " , isPremium = " + itemMaskRequest.getIsPremium())
                        .data(null)
                        .build();
            }



        }
        return null;
    }
}
