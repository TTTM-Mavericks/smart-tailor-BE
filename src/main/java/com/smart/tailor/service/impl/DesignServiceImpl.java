package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Design;
import com.smart.tailor.entities.PartOfDesign;
import com.smart.tailor.enums.RoleType;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ExternalServiceException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.DesignMapper;
import com.smart.tailor.repository.DesignRepository;
import com.smart.tailor.service.DesignService;
import com.smart.tailor.service.ExpertTailoringService;
import com.smart.tailor.service.PartOfDesignService;
import com.smart.tailor.service.UserService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.CloneDesignRequest;
import com.smart.tailor.utils.request.DesignRequest;
import com.smart.tailor.utils.request.PartOfDesignRequest;
import com.smart.tailor.utils.request.UpdateDesignRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.DesignResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
    public void addNewDesign(DesignRequest designRequest) {
        if(!Utilities.isValidBoolean(designRequest.getPublicStatus())){
            throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " publicStatus");
        }

        var user = userService.getUserByUserID(UUID.fromString(designRequest.getUserID()))
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.USER_IS_NOT_FOUND));

        var expertTailoringResponse = expertTailoringService.findExpertTailoringByID(UUID.fromString(designRequest.getExpertTailoringID()))
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_EXPERT_TAILORING));

        String color = Optional.ofNullable(designRequest.getColor()).orElse(null);

        Design design = designRepository.save(
                Design
                        .builder()
                        .user(user)
                        .expertTailoring(expertTailoringResponse)
                        .titleDesign(designRequest.getTitleDesign())
                        .publicStatus(designRequest.getPublicStatus())
                        .color(color)
                        .build()
        );

        if(Optional.ofNullable(designRequest.getPartOfDesign()).isEmpty()){
            throw new BadRequestException(MessageConstant.PART_OF_DESIGN_LIST_REQUEST_IS_EMPTY);
        }

        List<PartOfDesign> partOfDesignList = null;
        try{
            partOfDesignList = partOfDesignService.createPartOfDesign(design, designRequest.getPartOfDesign());
        }  catch (BadRequestException ex) {
            logger.error("Bad Request Exception in create Part Of Design {}",ex.getMessage());
            throw new BadRequestException(ex.getMessage());
        } catch(ItemNotFoundException ex){
            logger.error("Item Not Found Exception in Part Of Design {}",ex.getMessage());
            throw new ItemNotFoundException(ex.getMessage());
        }

        byte[] imageUrl = Optional.ofNullable(partOfDesignList)
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(part -> part.getPartOfDesignName().toLowerCase().contains("front"))
                .map(partOfDesign -> partOfDesign.getImageUrl())
                .findFirst()
                .orElse(null);

        // Set ImageUrl From Front PartOfDesign to Design
        design.setImageUrl(imageUrl);

        // Update List PartOfDesign belong to Design
        design.setPartOfDesignList(partOfDesignList);

        designRepository.save(design);
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

        var userExisted = userService.getUserByUserID(userID)
                .orElseThrow(() -> new  ItemNotFoundException(MessageConstant.USER_IS_NOT_FOUND));

        if (!userExisted.getRoles().getRoleName().contains(roleName)) {
            throw new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ROLE);
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
    public void updatePublicStatusDesign(UUID designID) {
        var designExisted = designRepository.findById(designID)
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_DESIGN));

        designExisted.setPublicStatus(!designExisted.getPublicStatus());
        designRepository.save(designExisted);
    }

    @Transactional
    @Override
    public void addNewCloneDesignFromBrandDesign(CloneDesignRequest cloneDesignRequest) {
        var user = userService.getUserByUserID(UUID.fromString(cloneDesignRequest.getUserID()))
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.USER_IS_NOT_FOUND));

        var brandDesign = designRepository.findById(UUID.fromString(cloneDesignRequest.getDesignID()))
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_DESIGN_BY_BRAND_ID));

        Design cloneDesign = designRepository.save(
                Design
                        .builder()
                        .user(user)
                        .expertTailoring(brandDesign.getExpertTailoring())
                        .titleDesign(brandDesign.getTitleDesign())
                        .publicStatus(brandDesign.getPublicStatus())
                        .color(brandDesign.getColor())
                        .build()
        );
        logger.info("Create Clone Design {}", cloneDesign);

        if(Optional.ofNullable(cloneDesignRequest.getPartOfDesign()).isEmpty()){
            throw new BadRequestException(MessageConstant.PART_OF_DESIGN_LIST_REQUEST_IS_EMPTY);
        }

        List<PartOfDesign> partOfDesignList = null;
        try{
            partOfDesignList = partOfDesignService.createPartOfDesign(cloneDesign, cloneDesignRequest.getPartOfDesign());
        }  catch (BadRequestException ex) {
            logger.error("Bad Request Exception in create Part Of Design {}",ex.getMessage());
            throw new BadRequestException(ex.getMessage());
        } catch(ItemNotFoundException ex){
            logger.error("Item Not Found Exception in Part Of Design {}",ex.getMessage());
            throw new ItemNotFoundException(ex.getMessage());
        }

        byte[] imageUrl = Optional.ofNullable(partOfDesignList)
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(part -> part.getPartOfDesignName().toLowerCase().contains("front"))
                .map(partOfDesign -> partOfDesign.getImageUrl())
                .findFirst()
                .orElse(null);

        // Set ImageUrl From Front PartOfDesign to Design
        cloneDesign.setImageUrl(imageUrl);

        // Update List PartOfDesign belong to Design
        cloneDesign.setPartOfDesignList(partOfDesignList);

        designRepository.save(cloneDesign);
    }

    @Transactional
    @Override
    public void updateDesign(UpdateDesignRequest updateDesignRequest) {
        var design = designRepository.findById(UUID.fromString(updateDesignRequest.getDesignID()))
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_DESIGN));

        List<PartOfDesign> partOfDesignList = null;
        try{
            design.getPartOfDesignList().clear();
            partOfDesignList = partOfDesignService.updatePartOfDesign(design, updateDesignRequest.getPartOfDesign());
        }  catch (BadRequestException ex) {
            logger.error("Bad Request Exception in Update Part Of Design {}",ex.getMessage());
            throw new BadRequestException(ex.getMessage());
        } catch(ItemNotFoundException ex){
            logger.error("Item Not Found Exception in Update Part Of Design {}",ex.getMessage());
            throw new ItemNotFoundException(ex.getMessage());
        }

        byte[] imageUrl = Optional.ofNullable(partOfDesignList)
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(part -> part.getPartOfDesignName().toLowerCase().contains("front"))
                .map(partOfDesign -> partOfDesign.getImageUrl())
                .findFirst()
                .orElse(null);

        // Set ImageUrl From Front PartOfDesign to Design
        design.setImageUrl(imageUrl);

        // Update List PartOfDesign belong to Design
        design.setPartOfDesignList(partOfDesignList);

        designRepository.save(design);
    }
}
