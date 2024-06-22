package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Design;
import com.smart.tailor.entities.PartOfDesign;
import com.smart.tailor.entities.User;
import com.smart.tailor.enums.RoleType;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ExternalServiceException;
import com.smart.tailor.exception.NotFoundException;
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
        try{
            if(!Utilities.isStringNotNullOrEmpty(designRequest.getUserEmail())){
                throw new BadRequestException(MessageConstant.DATA_IS_EMPTY + " UserEmail");
            }

            if(!Utilities.isStringNotNullOrEmpty(designRequest.getExpertTailoringName())){
                throw new BadRequestException(MessageConstant.DATA_IS_EMPTY + " ExpertTailoringName");
            }

            if(!Utilities.isStringNotNullOrEmpty(designRequest.getTitleDesign())){
                throw new BadRequestException(MessageConstant.DATA_IS_EMPTY + " TitleDesign");
            }

            if(!Utilities.isValidBoolean(designRequest.getPublicStatus())){
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " publicStatus");
            }

            if(!Utilities.isValidEmail(designRequest.getUserEmail())){
                throw new BadRequestException(MessageConstant.INVALID_EMAIL + " userEmail");
            }

            var user = userService.getUserDetailByEmail(designRequest.getUserEmail())
                    .orElseThrow(() -> new NotFoundException(MessageConstant.USER_IS_NOT_FOUND));

            var expertTailoringResponse = expertTailoringService.getByExpertTailoringName(designRequest.getExpertTailoringName());

            if (expertTailoringResponse == null){
                throw new NotFoundException(MessageConstant.CAN_NOT_FIND_ANY_EXPERT_TAILORING);
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
        catch (BadRequestException e){
            logger.error("INSIDE BAD REQUEST EXCEPTION createDesign Method");
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
        catch (NotFoundException e){
            logger.error("INSIDE NOT FOUND EXCEPTION createDesign Method");
            return APIResponse
                    .builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
        catch (ExternalServiceException e) {
            logger.error("INSIDE EXTERNAL SERVICE EXCEPTION createDesign Method");
            return APIResponse.builder()
                    .status(e.getHttpStatus().value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
        catch (Exception e){
            logger.error("Exception createDesign Method");
            return APIResponse
                    .builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(MessageConstant.ADD_NEW_DESIGN_FAIL + " : " + e.getMessage())
                    .data(null)
                    .build();
        }
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
        try{
            if (!Utilities.isValidUUIDType(userID)) {
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " userID");
            }

            if (!Utilities.isStringNotNullOrEmpty(roleName)) {
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " roleName");
            }

            var userExisted = userService.getUserByUserID(userID);
            if (userExisted == null) {
                throw new NotFoundException(MessageConstant.USER_IS_NOT_FOUND);
            }

            if (!userExisted.getRoles().getRoleName().contains(roleName)) {
                throw new NotFoundException(MessageConstant.CAN_NOT_FIND_ROLE);
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
        catch (BadRequestException e){
            logger.error("BAD REQUEST EXCEPTION getAllDesignByUserIDAndRoleName Method");
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
        catch (NotFoundException e){
            logger.error("NOT FOUND EXCEPTION getAllDesignByUserIDAndRoleName Method");
            return APIResponse
                    .builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
        catch (Exception e){
            logger.error("ERROR getAllDesignByUserIDAndRoleName Method");
            return APIResponse
                    .builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(MessageConstant.GET_ALL_DESIGN_FAIL + " : " + e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    public APIResponse updatePublicStatusDesign(UUID designID) {
        try{
            if (!Utilities.isValidUUIDType(designID)) {
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " designID");
            }

            var designExisted = getDesignByID(designID);
            if (designExisted == null) {
                throw new NotFoundException(MessageConstant.CAN_NOT_FIND_ANY_DESIGN);
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
        catch (BadRequestException e){
            logger.error("BAD REQUEST EXCEPTION updatePublicStatusDesign Method");
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
        catch (NotFoundException e){
            logger.error("NOT FOUND EXCEPTION updatePublicStatusDesign Method");
            return APIResponse
                    .builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
        catch (Exception e){
            logger.error("ERROR INSIDE updatePublicStatusDesign Method");
            return APIResponse
                    .builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(MessageConstant.UPDATE_PUBLIC_STATUS_FAIL + " : " + e.getMessage())
                    .data(null)
                    .build();
        }
    }
}
