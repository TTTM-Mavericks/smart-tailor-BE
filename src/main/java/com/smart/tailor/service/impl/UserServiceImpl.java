package com.smart.tailor.service.impl;


import com.smart.tailor.entities.User;
import com.smart.tailor.mapper.UserMapper;
import com.smart.tailor.repository.UserRepository;
import com.smart.tailor.service.ImageService;
import com.smart.tailor.service.RoleService;
import com.smart.tailor.service.UserService;
import com.smart.tailor.service.UsingImageService;
import com.smart.tailor.utils.request.UserRequest;
import com.smart.tailor.utils.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UsingImageService usingImageService;
    private final ImageService imageService;

    @Override
    public Optional<User> getUserDetailByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    @Override
    public void saveOrUpdateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User registerNewUsers(UserRequest userRequest) {
        User savedUser = null;
        if (!userRequest.getEmail().isEmpty() && !userRequest.getEmail().isBlank() && !userRequest.getPassword().isEmpty() && !userRequest.getPassword().isBlank()) {
            savedUser = userRepository.save(
                    User
                            .builder()
                            .email(userRequest.getEmail())
                            .password(userRequest.getPassword())
                            .language(userRequest.getLanguage())
                            .provider(userRequest.getProvider())
                            .userStatus(true)
                            .fullName(userRequest.getFullName())
                            .phoneNumber(userRequest.getPhoneNumber())
                            .roles(roleService.findRoleByRoleName("CUSTOMER").get())
                            .build());
        }
        return savedUser;
    }

    @Override
    public List<UserResponse> getAllUserResponse() {
        return userRepository.findAll().stream().map(this::convertToUserResponse).toList();
    }

    public UserResponse convertToUserResponse(User user) {
        UserResponse userResponse = userMapper.mapperToUserResponse(user);
        UUID imageID = usingImageService.getImage("AVATAR", userResponse.getUserID());
        System.out.println(imageID);
        if (imageID != null) {
            String url = imageService.getImageUrl(imageID);
            if (url != null) {
                userResponse.setAvatar(url);
            }
            System.out.println(url);
        }
        return userResponse;
    }
}
