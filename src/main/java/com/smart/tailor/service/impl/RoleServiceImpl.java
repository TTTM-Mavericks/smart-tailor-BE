package com.smart.tailor.service.impl;

import com.smart.tailor.entities.Roles;
import com.smart.tailor.repository.RoleRepository;
import com.smart.tailor.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Optional<Roles> findRoleByRoleName(String role_name) {
        return roleRepository.findByRoleName(role_name);
    }

    @Override
    public List<Roles> findAllRole() {
        return roleRepository.findAll();
    }

    @Override
    public void createRole(Roles roles) {
        roleRepository.save(roles);
    }
}
