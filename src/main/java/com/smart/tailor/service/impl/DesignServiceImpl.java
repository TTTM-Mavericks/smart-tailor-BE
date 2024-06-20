package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Design;
import com.smart.tailor.entities.User;
import com.smart.tailor.mapper.DesignMapper;
import com.smart.tailor.repository.DesignRepository;
import com.smart.tailor.service.DesignService;
import com.smart.tailor.service.ExpertTailoringService;
import com.smart.tailor.service.PartOfDesignService;
import com.smart.tailor.service.UserService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.DesignRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.DesignResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Override
    public APIResponse createDesign(DesignRequest designRequest) {
        if(
                !Utilities.isStringNotNullOrEmpty(designRequest.getUserEmail()) ||
                !Utilities.isStringNotNullOrEmpty(designRequest.getExpertTailoringName()) ||
                !Utilities.isStringNotNullOrEmpty(designRequest.getTitleDesign()) ||
                !Utilities.isStringNotNullOrEmpty(designRequest.getImageUrl()) ||
                !Utilities.isStringNotNullOrEmpty(designRequest.getColor())) {

            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.DATA_IS_EMPTY)
                    .data(null)
                    .build();
        }

        if(!Utilities.isValidBoolean(designRequest.getPublicStatus())){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.INVALID_DATA_TYPE + " , publicStatus = " + designRequest.getPublicStatus())
                    .data(null)
                    .build();
        }

        if(!Utilities.isValidEmail(designRequest.getUserEmail())){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.INVALID_EMAIL + " , userEmail =  " + designRequest.getUserEmail())
                    .data(null)
                    .build();
        }

        Optional<User> userOptional = userService.getUserDetailByEmail(designRequest.getUserEmail());
        if(userOptional.isEmpty()){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.USER_IS_NOT_FOUND)
                    .data(null)
                    .build();
        }

        var expertTailoringResponse = expertTailoringService.getByExpertTailoringName(designRequest.getExpertTailoringName());

        if (expertTailoringResponse == null){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.CAN_NOT_FIND_ANY_EXPERT_TAILORING)
                    .data(null)
                    .build();
        }

        Design design = designRepository.save(
                Design
                    .builder()
                    .user(userOptional.get())
                    .expertTailoringName(designRequest.getExpertTailoringName())
                    .titleDesign(designRequest.getTitleDesign())
                    .publicStatus(designRequest.getPublicStatus())
                    .imageUrl(designRequest.getImageUrl())
                    .color(designRequest.getColor())
                    .build()
        );

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
                .map(design -> designMapper.mapperToDesignResponse(design))
                .toList();
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
                .map(design -> designMapper.mapperToDesignResponse(design))
                .toList();
    }
}
