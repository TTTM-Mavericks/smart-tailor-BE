package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.ItemMask;
import com.smart.tailor.entities.PartOfDesign;
import com.smart.tailor.enums.PrintType;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.ItemMaskMapper;
import com.smart.tailor.repository.ItemMaskRepository;
import com.smart.tailor.service.ItemMaskService;
import com.smart.tailor.service.MaterialService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.ItemMaskRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.ItemMaskResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemMaskServiceImpl implements ItemMaskService {
    private final ItemMaskRepository itemMaskRepository;
    private final ItemMaskMapper itemMaskMapper;
    private final MaterialService materialService;
    private final Logger logger = LoggerFactory.getLogger(ItemMaskServiceImpl.class);

    @Override
    @Transactional
    public APIResponse createItemMask(PartOfDesign partOfDesign, List<ItemMaskRequest> itemMaskRequestList) {
        List<ItemMask> itemMaskList = new ArrayList<>();

        for(ItemMaskRequest itemMaskRequest : itemMaskRequestList){
            if(!Utilities.isValidBoolean(itemMaskRequest.getIsSystemItem())){
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " isSystemItem");
            }

            if(!Utilities.isValidFloat(itemMaskRequest.getPositionX())){
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " positionX");
            }

            if(!Utilities.isValidFloat(itemMaskRequest.getPositionY())){
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " positionY");
            }

            if(!Utilities.isValidFloat(itemMaskRequest.getScaleX())){
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " scaleX");
            }

            if(!Utilities.isValidFloat(itemMaskRequest.getScaleY())){
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " scaleY");
            }

            if(!Utilities.isValidInteger(itemMaskRequest.getIndexZ())){
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " indexZ");
            }

            // Check Whether ImageUrl is existed or not. Then Convert It to Base64
            byte[] base64ImageUrl = null;
            if(Optional.ofNullable(itemMaskRequest.getImageUrl()).isPresent()){
                base64ImageUrl = Utilities.encodeStringToBase64(itemMaskRequest.getImageUrl());
            }

            String itemMaskName = Optional.ofNullable(itemMaskRequest.getItemMaskName()).orElse(null);
            String typeOfItem = Optional.ofNullable(itemMaskRequest.getTypeOfItem()).orElse(null);

            var material = materialService.findByMaterialName(itemMaskRequest.getMaterialName())
                    .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CATEGORY_AND_MATERIAL_IS_NOT_EXISTED));

            var itemMask = itemMaskRepository.save(
                    ItemMask
                            .builder()
                            .partOfDesign(partOfDesign)
                            .itemMaskName(itemMaskName)
                            .material(material)
                            .typeOfItem(typeOfItem)
                            .isSystemItem(itemMaskRequest.getIsSystemItem())
                            .positionX(itemMaskRequest.getPositionX())
                            .positionY(itemMaskRequest.getPositionY())
                            .scaleX(itemMaskRequest.getScaleX())
                            .scaleY(itemMaskRequest.getScaleY())
                            .indexZ(itemMaskRequest.getIndexZ())
                            .imageUrl(base64ImageUrl)
                            .printType(PrintType.valueOf(itemMaskRequest.getPrintType()))
                            .build()
            );

            itemMaskList.add(itemMask);
        }

        return APIResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message(MessageConstant.ADD_ITEM_MASK_SUCCESSFULLY)
                .data(itemMaskList)
                .build();
    }

    @Override
    public List<ItemMaskResponse> getListItemMaskByPartOfDesignID(UUID partOfDesignID) {
        return itemMaskRepository
                .findAll()
                .stream()
                .filter(itemMask -> itemMask.getPartOfDesign().getPartOfDesignID().toString().equals(partOfDesignID.toString()))
                .map(itemMaskMapper::mapperToItemMaskResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ItemMaskResponse getItemMaskByItemMaskID(UUID itemMaskID) {
        var itemMask = itemMaskRepository.findById(itemMaskID);
        if(itemMask.isPresent()){
            return itemMaskMapper.mapperToItemMaskResponse(itemMask.get());
        }
        return null;
    }

    @Override
    public List<ItemMaskResponse> getAllItemMask() {
        return itemMaskRepository
                .findAll()
                .stream()
                .map(itemMaskMapper::mapperToItemMaskResponse)
                .collect(Collectors.toList());
    }
}
