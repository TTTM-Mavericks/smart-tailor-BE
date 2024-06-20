package com.smart.tailor.service;


import com.smart.tailor.entities.User;
import com.smart.tailor.enums.UserStatus;
import com.smart.tailor.utils.request.UserRequest;
import com.smart.tailor.utils.response.UserResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserDetailByEmail(String email);

    User getUserByPhoneNumber(String phoneNumber);

    User getUserByEmail(String email);

    void saveOrUpdateUser(User user);

    UserResponse convertToUserResponse(User user);

    User registerNewUsers(UserRequest userRequest) throws Exception;

    List<UserResponse> getAllUserResponse();

    Boolean updateStatusAccount(String email, UserStatus userStatus);

    User updateUserProfile(UserRequest userRequest);

}
