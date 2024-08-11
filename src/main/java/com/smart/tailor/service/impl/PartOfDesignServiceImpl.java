package com.smart.tailor.service.impl;

import com.smart.tailor.entities.Design;
import com.smart.tailor.entities.ItemMask;
import com.smart.tailor.entities.Material;
import com.smart.tailor.entities.PartOfDesign;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.PartOfDesignMapper;
import com.smart.tailor.repository.PartOfDesignRepository;
import com.smart.tailor.service.ItemMaskService;
import com.smart.tailor.service.MaterialService;
import com.smart.tailor.service.PartOfDesignService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.PartOfDesignRequest;
import com.smart.tailor.utils.response.PartOfDesignResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        for (PartOfDesignRequest partOfDesignRequest : partOfDesignRequestList) {
            // Check Whether ImageUrl is existed or not. Then Convert It to Base64
            byte[] base64ImageUrl = null;
            if (Optional.ofNullable(partOfDesignRequest.getImageUrl()).isPresent()) {
                base64ImageUrl = Utilities.encodeStringToBase64(partOfDesignRequest.getImageUrl());
            }

            // Check Whether SuccessImageUrl is existed or not. Then Convert It to Base64
            byte[] base64SuccessImageUrl = null;
            if (Optional.ofNullable(partOfDesignRequest.getSuccessImageUrl()).isPresent()) {
                base64SuccessImageUrl = Utilities.encodeStringToBase64(partOfDesignRequest.getSuccessImageUrl());
            }

            byte[] base64RealPartImageUrl = null;
            if (Optional.ofNullable(partOfDesignRequest.getRealPartImageUrl()).isPresent()) {
                base64RealPartImageUrl = Utilities.encodeStringToBase64(partOfDesignRequest.getRealPartImageUrl());
            }

            var partOfDesign = PartOfDesign
                    .builder()
                    .design(design)
                    .partOfDesignName(partOfDesignRequest.getPartOfDesignName())
                    .imageUrl(base64ImageUrl)
                    .successImageUrl(base64SuccessImageUrl)
                    .realPartImageUrl(base64RealPartImageUrl)
                    .width(partOfDesignRequest.getWidth())
                    .height(partOfDesignRequest.getHeight())
                    .build();


            Material material = null;
            if (Utilities.isStringNotNullOrEmpty(partOfDesignRequest.getMaterialID())) {
                if (!Utilities.isValidCustomKey(partOfDesignRequest.getMaterialID())) {
                    throw new BadRequestException("Invalid Type String of MaterialID: " + partOfDesignRequest.getMaterialID());
                }

                material = materialService.findMaterialByID(partOfDesignRequest.getMaterialID()).
                        orElseThrow(() -> new ItemNotFoundException("Can not find Material with MaterialID: " + partOfDesignRequest.getMaterialID()));

                partOfDesign.setMaterial(material);
            } else {
                var expertTailoring = design.getExpertTailoring();
                var materials = materialService.findMaterialsByExpertTailoringIDAndCategoryName(expertTailoring.getExpertTailoringID(), "Fabric");
                partOfDesign.setMaterial(materials.get(0));
            }

            var savedPartOfDesign = partOfDesignRepository.save(partOfDesign);

            if (Optional.ofNullable(partOfDesignRequest.getItemMask()).isEmpty()) {
                continue;
            }
            List<ItemMask> itemMaskList = null;
            try {
                itemMaskList = itemMaskService.createItemMask(savedPartOfDesign, partOfDesignRequest.getItemMask());
            } catch (BadRequestException ex) {
                logger.error("Bad Request Exception in create Item Mask {}", ex.getMessage());
                throw new BadRequestException(ex.getMessage());
            } catch (ItemNotFoundException ex) {
                logger.error("Item Not Found Exception in create Item Mask {}", ex.getMessage());
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
    public List<PartOfDesignResponse> getListPartOfDesignByDesignID(String designID) {
        return partOfDesignRepository
                .findAll()
                .stream()
                .filter(part -> part.getDesign().getDesignID().toString().equals(designID.toString()))
                .map(partOfDesignMapper::mapperToPartOfDesignResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PartOfDesign> getListPartOfDesignObjectByDesignID(String designID) {
        return partOfDesignRepository
                .findAll()
                .stream()
                .filter(part -> part.getDesign().getDesignID().toString().equals(designID.toString()))
                .collect(Collectors.toList());
    }

    @Override
    public PartOfDesignResponse getPartOfDesignByPartOfDesignID(String partOfDesignID) {
        var partOfDesign = partOfDesignRepository.findById(partOfDesignID);
        if (partOfDesign.isPresent()) {
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
    public void deletePartOfDesignByDesignID(String designID) {
        itemMaskService.deleteItemMaskByDesignID(designID);
        partOfDesignRepository.deletePartOfDesignByDesignID(designID);
    }

    @Override
    public List<PartOfDesign> savePartOfDesign(List<PartOfDesign> partOfDesignList) {
        return partOfDesignRepository.saveAll(partOfDesignList);
    }
}
