package com.smart.tailor.service;


import com.smart.tailor.entities.User;
import com.smart.tailor.utils.request.UserRequest;
import com.smart.tailor.utils.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserDetailByEmail(String email);

    User getUserByEmail(String email);

    void saveOrUpdateUser(User user);

    UserResponse convertToUserResponse(User user);

    User registerNewUsers(UserRequest userRequest);

    List<UserResponse> getAllUserResponse();

    Boolean updateStatusAccount(String email);

    UserResponse getUserByPhoneNumber(String phoneNumber);
}
