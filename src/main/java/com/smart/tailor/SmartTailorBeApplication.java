package com.smart.tailor;

import com.smart.tailor.entities.Roles;
import com.smart.tailor.entities.User;
import com.smart.tailor.enums.BrandStatus;
import com.smart.tailor.enums.Provider;
import com.smart.tailor.enums.UserStatus;
import com.smart.tailor.repository.BrandRepository;
import com.smart.tailor.repository.RoleRepository;
import com.smart.tailor.repository.UserRepository;
import com.smart.tailor.service.RoleService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.NotificationRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@EnableJpaAuditing()
@EnableCaching
public class SmartTailorBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartTailorBeApplication.class, args);

    }

    @Bean
    public CommandLineRunner createRoles(RoleService roleService) {
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

    @Bean
    public CommandLineRunner createSampleBrand(UserRepository userRepository,
                                               BrandRepository brandRepository,
                                               PasswordEncoder passwordEncoder,
                                               RoleRepository roleRepository) {
        return args -> {
            if(userRepository.findAll().size() == 0){
                User userTest1 = userRepository.save(User.builder()
                        .email("lalisa@example.com")
                        .password(passwordEncoder.encode("HASH_PASSWORD"))
                        .phoneNumber(Utilities.generateRandomNumber())
                        .userStatus(UserStatus.ACTIVE)
                        .provider(Provider.LOCAL)
                        .roles(roleRepository.findByRoleName("CUSTOMER").orElse(null))
                        .build()
                );

                User userTest2 = userRepository.save(User.builder()
                        .email("goyounjung@example.com")
                        .password(passwordEncoder.encode("HASH_PASSWORD"))
                        .phoneNumber(Utilities.generateRandomNumber())
                        .userStatus(UserStatus.ACTIVE)
                        .provider(Provider.LOCAL)
                        .roles(roleRepository.findByRoleName("CUSTOMER").orElse(null))
                        .build()
                );

                brandRepository.createShortBrand(userTest1.getUserID(),"LA LA LISA BRAND", BrandStatus.ACCEPT.name());
                brandRepository.createShortBrand(userTest2.getUserID(),"GO YOUN JUNG BRAND", BrandStatus.ACCEPT.name());
            }
        };
    }
}
