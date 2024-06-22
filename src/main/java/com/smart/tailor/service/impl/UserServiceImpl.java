package com.smart.tailor.service.impl;


import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Roles;
import com.smart.tailor.entities.User;
import com.smart.tailor.enums.Provider;
import com.smart.tailor.enums.UserStatus;
import com.smart.tailor.mapper.UserMapper;
import com.smart.tailor.repository.UserRepository;
import com.smart.tailor.service.RoleService;
import com.smart.tailor.service.UserService;
import com.smart.tailor.utils.Utilities;
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

    @Override
    public Optional<User> getUserDetailByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        return userRepository.getByPhoneNumber(phoneNumber);
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
    public User registerNewUsers(UserRequest userRequest) throws Exception {
        User savedUser = null;
        try {

            Optional<Roles> role = roleService.findRoleByRoleName(userRequest.getRoleName().trim().toUpperCase());
            if (!role.isPresent()) {
                throw new Exception(MessageConstant.CAN_NOT_FIND_ROLE + " " + userRequest.getRoleName());
            }

            if (!userRequest.getEmail().isEmpty() && !userRequest.getEmail().isBlank() && !userRequest.getPassword().isEmpty() && !userRequest.getPassword().isBlank()) {
                savedUser = userRepository.save(
                        User
                            .builder()
                            .email(userRequest.getEmail())
                            .password(userRequest.getPassword())
                            .language(userRequest.getLanguage())
                            .provider(userRequest.getProvider())
                            .userStatus(userRequest.getProvider().equals(Provider.LOCAL) ? UserStatus.INACTIVE : UserStatus.ACTIVE)
                            .fullName(userRequest.getFullName())
                            .phoneNumber(userRequest.getPhoneNumber())
                            .roles(role.get())
                            .imageUrl(userRequest.getImageUrl())
                            .build()
                );
            }
            return savedUser;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public List<UserResponse> getAllUserResponse() {
        return userRepository.findAll().stream().map(this::convertToUserResponse).toList();
    }

    public UserResponse convertToUserResponse(User user) {
        return userMapper.mapperToUserResponse(user);
    }

    @Override
    public Boolean updateStatusAccount(String email, UserStatus userStatus) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            user.get().setUserStatus(userStatus);
            userRepository.save(user.get());
            return true;
        }
        return false;
    }

    @Override
    public User updateUserProfile(UserRequest userRequest) {
        User userExisted = userRepository.getByEmail(userRequest.getEmail());
        if (userExisted != null){
            userExisted.setFullName(userRequest.getFullName());
            userExisted.setImageUrl(userRequest.getImageUrl());
            userExisted.setPhoneNumber(userRequest.getPhoneNumber());
            return userRepository.save(userExisted);
        }
        return null;
    }

    @Override
    public User getUserByUserID(UUID uuid) {
        if(!Utilities.isValidUUIDType(uuid)) return null;
        return userRepository.findById(uuid).orElse(null);
    }
}
