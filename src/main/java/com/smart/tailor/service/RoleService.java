package com.smart.tailor.service;

import com.smart.tailor.entities.Roles;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Optional<Roles> findRoleByRoleName(String role_name);

    List<Roles> findAllRole();

    void createRole(Roles roles);

}
