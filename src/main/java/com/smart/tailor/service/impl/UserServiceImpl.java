package com.smart.tailor.service.impl;


import com.smart.tailor.entities.User;
import com.smart.tailor.mapper.UserMapper;
import com.smart.tailor.repository.UserRepository;
import com.smart.tailor.service.*;
import com.smart.tailor.utils.request.UserRequest;
import com.smart.tailor.utils.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
                            .imageUrl(userRequest.getImageUrl())
                            .build());
        }
        return savedUser;
    }

    @Override
    public List<UserResponse> getAllUserResponse() {
        return userRepository.findAll().stream().map(this::convertToUserResponse).toList();
    }

    public UserResponse convertToUserResponse(User user) {
        return userMapper.mapperToUserResponse(user);
    }

    @Override
    public Boolean updateStatusAccount(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent())
        {
            user.get().setUserStatus(user.get().getUserStatus() == true ? false : true);
            userRepository.save(user.get());
            return true;
        }
        return false;
    }
}
