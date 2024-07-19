package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.*;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.PartOfDesignMapper;
import com.smart.tailor.repository.PartOfDesignRepository;
import com.smart.tailor.service.ItemMaskService;
import com.smart.tailor.service.MaterialService;
import com.smart.tailor.service.PartOfDesignService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.PartOfDesignRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.PartOfDesignResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartOfDesignServiceImpl implements PartOfDesignService {
    private final PartOfDesignRepository partOfDesignRepository;
    private final ItemMaskService itemMaskService;
    private final MaterialService materialService;
    private final PartOfDesignMapper partOfDesignMapper;
    private final Logger logger = LoggerFactory.getLogger(PartOfDesignServiceImpl.class);

    @Override
    @Transactional(readOnly = true)
    public List<PartOfDesign> createPartOfDesign(Design design, List<PartOfDesignRequest> partOfDesignRequestList) {
        List<PartOfDesign> partOfDesignList = new ArrayList<>();
        for(PartOfDesignRequest partOfDesignRequest : partOfDesignRequestList){
            // Check Whether ImageUrl is existed or not. Then Convert It to Base64
            byte[] base64ImageUrl = null;
            if(Optional.ofNullable(partOfDesignRequest.getImageUrl()).isPresent()){
                 base64ImageUrl = Utilities.encodeStringToBase64(partOfDesignRequest.getImageUrl());
            }

            // Check Whether SuccessImageUrl is existed or not. Then Convert It to Base64
            byte[] base64SuccessImageUrl = null;
            if(Optional.ofNullable(partOfDesignRequest.getSuccessImageUrl()).isPresent()){
                base64SuccessImageUrl = Utilities.encodeStringToBase64(partOfDesignRequest.getSuccessImageUrl());
            }

            var partOfDesign =  PartOfDesign
                    .builder()
                    .design(design)
                    .partOfDesignName(partOfDesignRequest.getPartOfDesignName())
                    .imageUrl(base64ImageUrl)
                    .successImageUrl(base64SuccessImageUrl)
                    .build();


            Material material = null;
            if(Utilities.isStringNotNullOrEmpty(partOfDesignRequest.getMaterialID())){
                if(!Utilities.isValidUUIDType(partOfDesignRequest.getMaterialID())){
                    throw new BadRequestException("Invalid Type UUID of MaterialID: " + partOfDesignRequest.getMaterialID());
                }

                 material = materialService.findMaterialByID(UUID.fromString(partOfDesignRequest.getMaterialID())).
                     orElseThrow(() -> new ItemNotFoundException("Can not find Material with MaterialID: " + partOfDesignRequest.getMaterialID()));

                partOfDesign.setMaterial(material);
            } else {
                var expertTailoring = design.getExpertTailoring();
                var materials = materialService.findMaterialsByExpertTailoringIDAndCategoryName(expertTailoring.getExpertTailoringID(), "Fabric");
                partOfDesign.setMaterial(materials.get(0));
            }

            var savedPartOfDesign = partOfDesignRepository.save(partOfDesign);

            if(Optional.ofNullable(partOfDesignRequest.getItemMask()).isEmpty()){
                continue;
            }
            List<ItemMask> itemMaskList = null;
            try{
                itemMaskList = itemMaskService.createItemMask(savedPartOfDesign, partOfDesignRequest.getItemMask());
            } catch (BadRequestException ex) {
                logger.error("Bad Request Exception in create Item Mask {}",ex.getMessage());
                throw new BadRequestException(ex.getMessage());
            } catch(ItemNotFoundException ex){
                logger.error("Item Not Found Exception in create Item Mask {}",ex.getMessage());
                throw new ItemNotFoundException(ex.getMessage());
            }

            // Set List Of ItemMask belong to PartOfDesign
//            savedPartOfDesign.setItemMaskList(itemMaskList);
            // Add Correct PartOfDesign to ListPartOfDesign
            partOfDesignList.add(savedPartOfDesign);
        }
        return partOfDesignList;

    }

    @Override
    public List<PartOfDesignResponse> getListPartOfDesignByDesignID(UUID designID) {
        return partOfDesignRepository
                .findAll()
                .stream()
                .filter(part -> part.getDesign().getDesignID().toString().equals(designID.toString()))
                .map(partOfDesignMapper::mapperToPartOfDesignResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PartOfDesignResponse getPartOfDesignByPartOfDesignID(UUID partOfDesignID) {
        var partOfDesign = partOfDesignRepository.findById(partOfDesignID);
        if(partOfDesign.isPresent()){
            return partOfDesignMapper.mapperToPartOfDesignResponse(partOfDesign.get());
        }
        return null;
    }

    @Override
    public List<PartOfDesignResponse> getAllPartOfDesign() {
        return partOfDesignRepository
                .findAll()
                .stream()
                .map(partOfDesignMapper::mapperToPartOfDesignResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deletePartOfDesignByDesignID(UUID designID) {
        itemMaskService.deleteItemMaskByDesignID(designID);
        partOfDesignRepository.deletePartOfDesignByDesignID(designID);
    }
}
