package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Design;
import com.smart.tailor.entities.User;
import com.smart.tailor.repository.DesignRepository;
import com.smart.tailor.service.DesignService;
import com.smart.tailor.service.PartOfDesignService;
import com.smart.tailor.service.UserService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.DesignRequest;
import com.smart.tailor.utils.response.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DesignServiceImpl implements DesignService {
    private final DesignRepository designRepository;
    private final PartOfDesignService partOfDesignService;
    private final UserService userService;
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
                    .message(MessageConstant.MISSING_ARGUMENT)
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
                    .message(MessageConstant.EMAIL_IS_NOT_EXISTED)
                    .data(null)
                    .build();
        }

        var partOfDesignResponse = partOfDesignService.createPartOfDesign(designRequest.getPartOfDesignRequestList());

        return null;
    }

    @Override
    public Design getDesignByID(UUID designID) {
        return null;
    }
}
