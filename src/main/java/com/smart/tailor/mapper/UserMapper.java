package com.smart.tailor.mapper;

import com.smart.tailor.entities.User;
import com.smart.tailor.utils.request.UserRequest;
import com.smart.tailor.utils.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "roles.roleName", target = "roleName")
    UserResponse mapperToUserResponse(User user);
}
