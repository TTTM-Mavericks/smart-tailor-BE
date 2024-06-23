package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Design;
import com.smart.tailor.entities.PartOfDesign;
import com.smart.tailor.enums.RoleType;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ExternalServiceException;
import com.smart.tailor.exception.ResourceNotFoundException;
import com.smart.tailor.mapper.DesignMapper;
import com.smart.tailor.repository.DesignRepository;
import com.smart.tailor.service.DesignService;
import com.smart.tailor.service.ExpertTailoringService;
import com.smart.tailor.service.PartOfDesignService;
import com.smart.tailor.service.UserService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.DesignRequest;
import com.smart.tailor.utils.request.PartOfDesignRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.DesignResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DesignServiceImpl implements DesignService {
    private final DesignRepository designRepository;
    private final PartOfDesignService partOfDesignService;
    private final ExpertTailoringService expertTailoringService;
    private final UserService userService;
    private final DesignMapper designMapper;
    private final Logger logger = LoggerFactory.getLogger(DesignServiceImpl.class);

    @Transactional
    @Override
    public APIResponse createDesign(DesignRequest designRequest) {
        if(!Utilities.isValidBoolean(designRequest.getPublicStatus())){
            throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " publicStatus");
        }

        var user = userService.getUserDetailByEmail(designRequest.getUserEmail())
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.USER_IS_NOT_FOUND));

        var expertTailoringResponse = expertTailoringService.getByExpertTailoringName(designRequest.getExpertTailoringName());

        if (expertTailoringResponse == null){
            throw new ResourceNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_EXPERT_TAILORING);
        }

        String color = Optional.ofNullable(designRequest.getColor()).orElse(null);

        Design design = designRepository.save(
                Design
                        .builder()
                        .user(user)
                        .expertTailoringName(designRequest.getExpertTailoringName())
                        .titleDesign(designRequest.getTitleDesign())
                        .publicStatus(designRequest.getPublicStatus())
                        .color(color)
                        .build()
        );

        APIResponse partOfDesignResponse = partOfDesignService.createPartOfDesign(design, designRequest.getPartOfDesignList());
        if(partOfDesignResponse.getStatus() != HttpStatus.OK.value()){
            throw new ExternalServiceException(partOfDesignResponse.getMessage(), HttpStatus.valueOf(partOfDesignResponse.getStatus()));
        }

        var partOfDesignList = (List<PartOfDesign>) partOfDesignResponse.getData();

        String imageUrl = Optional.ofNullable(designRequest.getPartOfDesignList())
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(part -> part.getPartOfDesignName().toLowerCase().contains("front"))
                .map(PartOfDesignRequest::getImageUrl)
                .findFirst()
                .orElse(null);

        // Set ImageUrl From Front PartOfDesign to Design
        design.setImageUrl(imageUrl);

        // Update List PartOfDesign belong to Design
        design.setPartOfDesignList(partOfDesignList);

        return APIResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message(MessageConstant.ADD_NEW_DESIGN_SUCCESSFULLY)
                .data(designMapper.mapperToDesignResponse(design))
                .build();

    }

    @Override
    public Design getDesignByID(UUID designID) {
        return designRepository.findById(designID).orElse(null);
    }

    @Override
    public List<DesignResponse> getAllDesignByUserID(UUID userID) {
        return designRepository
                .findAll()
                .stream()
                .filter(design -> design.getUser().getUserID().toString().equals(userID.toString()))
                .map(designMapper::mapperToDesignResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DesignResponse getDesignResponseByID(UUID designID) {
        var designOptional = designRepository.findById(designID);
        if(designOptional.isPresent()){
            return designMapper.mapperToDesignResponse(designOptional.get());
        }
        return null;
    }

    @Override
    public List<DesignResponse> getAllDesign() {
        return designRepository
                .findAll()
                .stream()
                .map(designMapper::mapperToDesignResponse)
                .collect(Collectors.toList());
    }

    @Override
    public APIResponse getAllDesignByUserIDAndRoleName(UUID userID, String roleName) {
        if (!Utilities.isStringNotNullOrEmpty(roleName)) {
            throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " roleName");
        }

        var userExisted = userService.getUserByUserID(userID);
        if (userExisted == null) {
            throw new ResourceNotFoundException(MessageConstant.USER_IS_NOT_FOUND);
        }

        if (!userExisted.getRoles().getRoleName().contains(roleName)) {
            throw new ResourceNotFoundException(MessageConstant.CAN_NOT_FIND_ROLE);
        }

        var designResponse = designRepository
                .findAll()
                .stream()
                .filter(design -> {
                    var user = design.getUser();
                    if(user.getUserID().toString().equals(userID.toString()) && user.getRoles().getRoleName().contains(roleName)){
                        return true;
                    }
                    return false;
                })
                .map(designMapper::mapperToDesignResponse)
                .collect(Collectors.toList());

        String message = roleName.equals(RoleType.CUSTOMER.name()) ? MessageConstant.GET_ALL_DESIGN_BY_CUSTOMER_ID_SUCCESSFULLY : MessageConstant.GET_ALL_DESIGN_BY_BRAND_ID_SUCCESSFULLY;

        return APIResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .data(designResponse)
                .build();
    }


    @Override
    public APIResponse updatePublicStatusDesign(UUID designID) {
        var designExisted = getDesignByID(designID);
        if (designExisted == null) {
            throw new ResourceNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_DESIGN);
        }

        designExisted.setPublicStatus(designExisted.getPublicStatus() ? false : true);
        designRepository.save(designExisted);

        return APIResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message(MessageConstant.UPDATE_PUBLIC_STATUS_SUCCESSFULLY)
                .data(designMapper.mapperToDesignResponse(designExisted))
                .build();
    }
}
