package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.ItemMask;
import com.smart.tailor.entities.PartOfDesign;
import com.smart.tailor.enums.PrintType;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.mapper.ItemMaskMapper;
import com.smart.tailor.repository.ItemMaskRepository;
import com.smart.tailor.service.ItemMaskService;
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
    private final Logger logger = LoggerFactory.getLogger(ItemMaskServiceImpl.class);

    @Override
    @Transactional
    public APIResponse createItemMask(PartOfDesign partOfDesign, List<ItemMaskRequest> itemMaskRequestList) {
        try{
            List<ItemMask> itemMaskList = new ArrayList<>();

            for(ItemMaskRequest itemMaskRequest : itemMaskRequestList){
                if(!Utilities.isValidBoolean(itemMaskRequest.getIsSystemItem())){
                    throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " isSystemItem");
                }

                if(!Utilities.isValidBoolean(itemMaskRequest.getIsPremium())){
                    throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " isPremium");
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

                if(!Utilities.isStringNotNullOrEmpty(itemMaskRequest.getImageUrl())){
                    throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " imageUrl");
                }

                if(!Utilities.isStringNotNullOrEmpty(itemMaskRequest.getPrintType())){
                    throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " PrintType");
                }
                PrintType printType = null;
                try {
                    printType = PrintType.valueOf(itemMaskRequest.getPrintType());
                }
                catch (IllegalArgumentException illegalArgumentException){
                    throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " PrintType " + itemMaskRequest.getPrintType());
                }

                String itemMaskName = Optional.ofNullable(itemMaskRequest.getItemMaskName()).orElse(null);
                String typeOfItem = Optional.ofNullable(itemMaskRequest.getTypeOfItem()).orElse(null);

                var itemMask = itemMaskRepository.save(
                        ItemMask
                                .builder()
                                .partOfDesign(partOfDesign)
                                .itemMaskName(itemMaskName)
                                .typeOfItem(typeOfItem)
                                .isSystemItem(itemMaskRequest.getIsSystemItem())
                                .positionX(itemMaskRequest.getPositionX())
                                .positionY(itemMaskRequest.getPositionY())
                                .scaleX(itemMaskRequest.getScaleX())
                                .scaleY(itemMaskRequest.getScaleY())
                                .indexZ(itemMaskRequest.getIndexZ())
                                .printType(printType)
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
        catch (BadRequestException e){
            logger.error("INSIDE BAD REQUEST EXCEPTION createItemMask Method");
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
        catch (Exception e){
            logger.error(MessageConstant.ADD_ITEM_MASK_FAIL);
            return APIResponse
                    .builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(MessageConstant.ADD_ITEM_MASK_FAIL + " : " + e.getMessage())
                    .data(null)
                    .build();
        }
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
