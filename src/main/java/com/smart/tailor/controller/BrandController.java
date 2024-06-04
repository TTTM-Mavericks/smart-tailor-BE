package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Brand;
import com.smart.tailor.enums.Provider;
import com.smart.tailor.mapper.BrandMapper;
import com.smart.tailor.service.BrandService;
import com.smart.tailor.service.UserService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.BrandRequest;
import com.smart.tailor.utils.request.UserRequest;
import com.smart.tailor.utils.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(APIConstant.BrandAPI.BRAND)
@RequiredArgsConstructor
public class BrandController {
    private final Logger logger = LoggerFactory.getLogger(BrandController.class);
    private final BrandService brandService;
    private final UserService userService;
    private final BrandMapper brandMapper;

    @GetMapping(APIConstant.BrandAPI.GET_BRAND + "/{id}")
    public ResponseEntity<ObjectNode> getBrandById(@PathVariable("id") UUID id) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            if (id == null) {
                response.put("status", 400);
                response.put("message", MessageConstant.MISSING_ARGUMENT);
                return ResponseEntity.ok(response);
            }
            Optional<Brand> brand = brandService.getBrandById(id);
            if (!brand.isPresent() || brand.isEmpty()) {
                response.put("status", 200);
                response.put("message", MessageConstant.CAN_NOT_FIND_BRAND + " with id: " + id);
                return ResponseEntity.ok(response);
            }
            response.put("status", 200);
            response.put("message", MessageConstant.GET_BRAND_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(brandMapper.mapperToBrandResponse(brand.get())));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", -1);
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN GET BRAND BY ID. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping(APIConstant.BrandAPI.UPLOAD_BRAND_INFOR)
    public ResponseEntity<ObjectNode> uploadBrandInfor(@RequestBody BrandRequest brandRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            if (brandRequest == null) {
                respon.put("status", 401);
                respon.put("message", MessageConstant.MISSING_ARGUMENT);
                return ResponseEntity.ok(respon);
            }

            if (brandRequest.getBankName() == null || !Utilities.isNonNullOrEmpty(brandRequest.getBrandName())) {
                respon.put("status", 401);
                respon.put("message", MessageConstant.MISSING_ARGUMENT);
                return ResponseEntity.ok(respon);
            }

            /**
             * TODO
             * Phê duyệt như nào? gửi mail? xác minh ra sao khi data chưa có dưới DB?
             * muon manager thay brand
             * brand luu DB
             * user luu DB
             *
             * check brand infor => update status brand <=> update status user => PENDING + ACCEPT
             *
             * MANAGER lam gì?
             */
            var brand = brandService.saveBrand(brandRequest);
//            emailSenderService.sendEmail(userRequest.getEmail(), "Account Verification", emailText);

            if (brand != null) {
                respon.put("status", 200);
                respon.put("message", MessageConstant.REGISTER_NEW_BRAND_SUCCESSFULLY);
                respon.set("data", objectMapper.valueToTree(brandMapper.mapperToBrandResponse(brand)));
                return ResponseEntity.ok(respon);
            } else {
                respon.put("status", 400);
                respon.put("message", MessageConstant.REGISTER_NEW_BRAND_FAILED);
                return ResponseEntity.ok(respon);
            }
        } catch (Exception ex) {
            respon.put("status", -1);
            respon.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN UPLOAD BRAND INFOR. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @PostMapping(APIConstant.BrandAPI.ADD_NEW_BRAND)
    public ResponseEntity<ObjectNode> addNewBrand(@RequestBody UserRequest userRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            // Check if enough argument?
            if (userRequest == null || userRequest.getEmail() == null) {
                respon.put("status", 400);
                respon.put("message", MessageConstant.MISSING_ARGUMENT);
                return ResponseEntity.ok(respon);
            }

            String email = userRequest.getEmail();
            String password = userRequest.getPassword();

            // Check email is valid?
            if (!Utilities.isValidEmail(email)) {
                respon.put("status", 400);
                respon.put("message", MessageConstant.INVALID_EMAIL);
                return ResponseEntity.ok(respon);
            }

            // Check password is valid? Only check when it's not google registration
            if (userRequest.getProvider() != Provider.GOOGLE) {
                if (!Utilities.isValidPassword(password)) {
                    respon.put("status", 400);
                    respon.put("message", MessageConstant.INVALID_PASSWORD);
                    return ResponseEntity.ok(respon);
                }
            }

            // Check email is duplicated?
            if (userService.getUserByEmail(userRequest.getEmail()) != null) {
                respon.put("status", 409);
                respon.put("message", MessageConstant.DUPLICATE_REGISTERED_EMAIL);
                return ResponseEntity.ok(respon);
            }

            UserResponse userResponse = brandService.register(userRequest);
            if (userResponse == null) {
                respon.put("status", 400);
                respon.put("message", MessageConstant.REGISTER_NEW_USER_FAILED);
                return ResponseEntity.ok(respon);
            }
            respon.put("status", 200);
            respon.put("message", MessageConstant.REGISTER_NEW_USER_SUCCESSFULLY);
            respon.set("data", objectMapper.valueToTree(userResponse));
            return ResponseEntity.ok(respon);

        } catch (Exception ex) {
            respon.put("status", -1);
            respon.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN ADD NEW BRAND. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @GetMapping(APIConstant.BrandAPI.VERIFY)
    public ResponseEntity<ObjectNode> verifyAccount(@RequestParam("email") String email, @RequestParam("token") String token) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            boolean isVerified = brandService.verifyUser(email, token);
            if (isVerified) {
                respon.put("status", 200);
                respon.put("message", MessageConstant.ACCOUNT_VERIFIED_SUCCESSFULLY);
                return ResponseEntity.ok(respon);
            } else {
                respon.put("status", 401);
                respon.put("message", MessageConstant.INVALID_VERIFICATION_TOKEN);
                return ResponseEntity.ok(respon);
            }
        } catch (Exception ex) {
            respon.put("status", -1);
            respon.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN VERIFY ACCOUNT. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @GetMapping(APIConstant.BrandAPI.CHECK_VERIFY + "/{email}")
    public ResponseEntity<ObjectNode> checkVerify(@PathVariable("email") String email) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            boolean isVerified = brandService.checkVerify(email);
            if (isVerified) {
                respon.put("status", 200);
                respon.put("message", MessageConstant.ACCOUNT_IS_VERIFIED);
                return ResponseEntity.ok(respon);
            } else {
                respon.put("status", 401);
                respon.put("message", MessageConstant.ACCOUNT_NOT_VERIFIED);
                return ResponseEntity.ok(respon);
            }
        } catch (Exception ex) {
            respon.put("status", -1);
            respon.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN CHECK VERIFY BRAND. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }
}
