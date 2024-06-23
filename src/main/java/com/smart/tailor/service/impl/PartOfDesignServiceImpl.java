package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Design;
import com.smart.tailor.entities.ItemMask;
import com.smart.tailor.entities.PartOfDesign;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ExternalServiceException;
import com.smart.tailor.mapper.PartOfDesignMapper;
import com.smart.tailor.repository.PartOfDesignRepository;
import com.smart.tailor.service.ItemMaskService;
import com.smart.tailor.service.PartOfDesignService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.PartOfDesignRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.PartOfDesignResponse;
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
public class PartOfDesignServiceImpl implements PartOfDesignService {
    private final PartOfDesignRepository partOfDesignRepository;
    private final ItemMaskService itemMaskService;
    private final PartOfDesignMapper partOfDesignMapper;
    private final Logger logger = LoggerFactory.getLogger(PartOfDesignServiceImpl.class);

    @Override
    @Transactional
    public APIResponse createPartOfDesign(Design design, List<PartOfDesignRequest> partOfDesignRequestList) {
        List<PartOfDesign> partOfDesignList = new ArrayList<>();
        for(PartOfDesignRequest partOfDesignRequest : partOfDesignRequestList){

            String partOfDesignName = Optional.ofNullable(partOfDesignRequest.getPartOfDesignName()).orElse(null);
            String imageUrl = Optional.ofNullable(partOfDesignRequest.getImageUrl()).orElse(null);
            String successImageUrl = Optional.ofNullable(partOfDesignRequest.getSuccessImageUrl()).orElse(null);

            var partOfDesign = partOfDesignRepository.save(
                    PartOfDesign
                            .builder()
                            .design(design)
                            .partOfDesignName(partOfDesignName)
                            .imageUrl(imageUrl)
                            .successImageUrl(successImageUrl)
                            .build()
            );

            var itemMaskResponse = itemMaskService.createItemMask(partOfDesign, partOfDesignRequest.getItemMaskList());
            if(itemMaskResponse.getStatus() != HttpStatus.OK.value()){
                throw new ExternalServiceException(itemMaskResponse.getMessage(), HttpStatus.valueOf(itemMaskResponse.getStatus()));
            }

            // Set List Of ItemMask belong to PartOfDesign
            partOfDesign.setItemMaskList((List<ItemMask>) itemMaskResponse.getData());

            // Add Correct PartOfDesign to ListPartOfDesign
            partOfDesignList.add(partOfDesign);
        }
        return APIResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message(MessageConstant.ADD_PART_OF_DESIGN_SUCCESSFULLY)
                .data(partOfDesignList)
                .build();

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
}
