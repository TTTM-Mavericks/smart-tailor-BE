package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Brand;
import com.smart.tailor.entities.User;
import com.smart.tailor.enums.BrandStatus;
import com.smart.tailor.enums.Provider;
import com.smart.tailor.enums.UserStatus;
import com.smart.tailor.mapper.UserMapper;
import com.smart.tailor.repository.BrandRepository;
import com.smart.tailor.service.BrandService;
import com.smart.tailor.service.EmailSenderService;
import com.smart.tailor.service.RoleService;
import com.smart.tailor.service.UserService;
import com.smart.tailor.utils.request.BrandRequest;
import com.smart.tailor.utils.request.UserRequest;
import com.smart.tailor.utils.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final UserService userService;
    private final Map<String, Object> storageObject = new HashMap<>();
    private final Map<String, String> verifyAccount = new HashMap<>();
    private final Map<String, String> verified = new HashMap<>();
    private final Map<String, String> expiredTimeLink = new HashMap<>();
    private final EmailSenderService emailSenderService;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Override
    public Optional<Brand> getBrandById(UUID brandId) throws Exception {
        try {
            if (brandId == null || brandId.toString().isEmpty() || brandId.toString().isBlank()) {
                throw new Exception(MessageConstant.MISSING_ARGUMENT);
            }
            return brandRepository.getBrandByBrandID(brandId);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public Brand saveBrand(BrandRequest brandRequest) throws Exception {
        Brand savedBrand = null;
        try {
            if (brandRequest.getEmail() != null && !brandRequest.getEmail().isEmpty()) {
                var user = userService.getUserByEmail(brandRequest.getEmail().trim());
                if (user != null) {
                    savedBrand = brandRepository.save(
                            Brand.builder()
                                    .user(user)
                                    .brandName(brandRequest.getBrandName())
                                    .bankName(brandRequest.getBankName() != null && !brandRequest.getBrandName().trim().isEmpty() ? brandRequest.getAccountName() : null)
                                    .accountNumber(brandRequest.getAccountNumber() != null && !brandRequest.getAccountNumber().trim().isEmpty() ? brandRequest.getAccountNumber() : null)
                                    .accountName(brandRequest.getAccountName() != null ? brandRequest.getAccountName() : null)
                                    .brandStatus(BrandStatus.PENDING)
                                    .address(brandRequest.getAddress() != null && !brandRequest.getAddress().trim().isEmpty() ? brandRequest.getAddress() : null)
                                    .QR_Payment(brandRequest.getQR_Payment() != null && !brandRequest.getQR_Payment().trim().isEmpty() ? brandRequest.getQR_Payment() : null)
                                    .rating(0.0f)
                                    .numberOfViolations(0)
                                    .build()
                    );
                }
            }
            return savedBrand;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public UserResponse register(UserRequest userRequest) throws Exception {
        try {
            userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            Provider provider = userRequest.getProvider() != null ? userRequest.getProvider() : Provider.LOCAL;
            userRequest.setProvider(provider);

            if (provider == Provider.LOCAL) {
                // check if Object exist in HashMap
                UserRequest checkUserRequest = (UserRequest) storageObject.get(userRequest.getEmail());
                if (checkUserRequest != null) {
                    String oldToken = verifyAccount.get(userRequest.getEmail());
                    LocalDateTime expiredTime = LocalDateTime.parse(expiredTimeLink.get(userRequest.getEmail() + " expiredTime"));
                    verifyAccount.remove(oldToken);
                    expiredTimeLink.remove(expiredTime);
                    storageObject.remove(userRequest.getEmail());
                }

                // Store Object Class to HashMap
                storageObject.put(userRequest.getEmail(), (Object) userRequest);

                String token = UUID.randomUUID().toString();
                verifyAccount.put(userRequest.getEmail(), token);

                LocalDateTime now = LocalDateTime.now();
                LocalDateTime expiredLinkVerify = now.plusMinutes(5);
                expiredTimeLink.put(userRequest.getEmail() + " expiredTime", expiredLinkVerify.toString());

                logger.info("Before Mail Email : {}, token : {}", userRequest.getEmail(), verifyAccount.get(userRequest.getEmail()));

                String verificationUrl = "https://be.mavericks-tttm.studio/api/v1/brand/verify" + "?email=" + userRequest.getEmail() + "&token=" + token;

                String emailText = "<!DOCTYPE html>" + "<html>" + "<head>" + "    <meta charset='UTF-8'>" + "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" + "    <title>Account Verification</title>" + "    <style>" + "        body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }" + "        .container { width: 100%; padding: 20px; }" + "        .content { background-color: #ffffff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" + "        .header { font-size: 24px; font-weight: bold; color: #333333; }" + "        .message { font-size: 16px; color: #555555; }" + "        .button { display: inline-block; padding: 10px 20px; font-size: 16px; color: #ffffff; background-color: #4CAF50; text-align: center; text-decoration: none; border-radius: 5px; margin-top: 20px; }" + "    </style>" + "</head>" + "<body>" + "    <div class='container'>" + "        <div class='content'>" + "            <div class='header'>Verify Your Account</div>" + "            <div class='message'>Hi " + userRequest.getEmail() + ",</div>" + "            <div class='message'>Thank you for registering. To complete your registration, please verify your email by clicking the button below.</div>" + "            <a href='" + verificationUrl + "' class='button'>Verify Account</a>" + "            <div class='message'>If you did not register for an account, please ignore this email.</div>" + "        </div>" + "    </div>" + "</body>" + "</html>";
                emailSenderService.sendEmail(userRequest.getEmail(), "Account Verification", emailText);
                return new UserResponse();
            }
            var user = User.builder().email(userRequest.getEmail()).password(passwordEncoder.encode(userRequest.getPassword())).userStatus(UserStatus.ACTIVE).fullName(userRequest.getFullName()).roles(roleService.findRoleByRoleName("BRAND").get()).phoneNumber(userRequest.getPhoneNumber()).imageUrl(userRequest.getImageUrl()).language(userRequest.getLanguage()).provider(userRequest.getProvider()).build();
            return userMapper.mapperToUserResponse(user);
        } catch (Exception ex) {
            throw ex;
        }
    }

    // 5s => call check verify or not
    @Override
    public Boolean checkVerify(String email) {
        try {
            var registerUser = userService.getUserByEmail(email);
            return registerUser != null && registerUser.getUserStatus().equals(UserStatus.ACTIVE);
        } catch (Exception ex) {
            logger.error("ERROR IN BrandServiceImpl - checkVerify: {}", ex.getMessage());
        }
        return false;
    }

    // user click verify link
    @Override
    public Boolean verifyUser(String email, String token) throws Exception {
        LocalDateTime expiredTime = LocalDateTime.parse(expiredTimeLink.get(email + " expiredTime"));
        LocalDateTime currentTime = LocalDateTime.now();
        String oldToken = verifyAccount.get(email);
        if (currentTime.isAfter(expiredTime)) {
            verifyAccount.remove(oldToken);
            expiredTimeLink.remove(expiredTime);
            storageObject.remove(email);
            return false;
        }
        logger.info(" Get Token From HashMap {}", oldToken);
        if (oldToken.equals(token)) {
            UserRequest userRequest = (UserRequest) storageObject.get(email);
            var user = userService.registerNewUsers(userRequest);
            verified.put(email, "TRUE");
            verifyAccount.remove(oldToken);
            expiredTimeLink.remove(expiredTime);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Brand> findBrandByBrandName(String brandName) {
        return brandRepository.findBrandByBrandName(brandName);
    }

    @Override
    public Brand getBrandByEmail(String email) throws Exception {
        try {
            if (email == null || email.isEmpty() || email.isBlank()) {
                throw new Exception(MessageConstant.MISSING_ARGUMENT);
            }
            return brandRepository.findBrandByUserEmail(email);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public Brand updateBrand(Brand brand) throws Exception {
        try {
            Brand savedBrand = brandRepository.save(brand);
            return savedBrand;
        } catch (Exception ex) {
            throw ex;
        }
    }
}
