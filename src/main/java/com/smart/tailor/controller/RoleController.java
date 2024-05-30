package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIConstant.RoleAPI.ROLE)
@RequiredArgsConstructor
@Slf4j
public class RoleController {
    private final RoleService roleService;

    @GetMapping(APIConstant.RoleAPI.GET_ALL_ROLES)
    public ResponseEntity<ObjectNode> getAllRoles() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var listRole = roleService.findAllRole();
        response.put("success", 200);
        response.set("data", objectMapper.valueToTree(listRole));
        if (!listRole.isEmpty()) {
            response.put("message", "List Roles is not Empty");
        } else {
            response.put("message", "List Roles is Empty");
        }
        return ResponseEntity.ok(response);
    }
}
