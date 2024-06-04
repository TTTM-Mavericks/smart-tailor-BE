package com.smart.tailor;

import com.smart.tailor.entities.Roles;
import com.smart.tailor.service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing()
@EnableCaching
public class SmartTailorBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartTailorBeApplication.class, args);

    }

    @Bean
    public CommandLineRunner commandLineRunner(RoleService roleService) {
        return args -> {
            if (roleService.findAllRole().size() == 0) {

                Roles customerRole = Roles.builder().roleName("CUSTOMER").build();
                Roles adminRole = Roles.builder().roleName("ADMIN").build();
                Roles managerRole = Roles.builder().roleName("MANAGER").build();
                Roles employeeRole = Roles.builder().roleName("EMPLOYEE").build();
                Roles accountantRole = Roles.builder().roleName("ACCOUNTANT").build();
                Roles brandRole = Roles.builder().roleName("BRAND").build();

                roleService.createRole(customerRole);
                roleService.createRole(adminRole);
                roleService.createRole(managerRole);
                roleService.createRole(employeeRole);
                roleService.createRole(accountantRole);
                roleService.createRole(brandRole);

            }
        };
    }
}
