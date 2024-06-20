package com.smart.tailor;

import com.smart.tailor.entities.ExpertTailoring;
import com.smart.tailor.entities.Roles;
import com.smart.tailor.entities.User;
import com.smart.tailor.enums.BrandStatus;
import com.smart.tailor.enums.Provider;
import com.smart.tailor.enums.UserStatus;
import com.smart.tailor.repository.*;
import com.smart.tailor.service.RoleService;
import com.smart.tailor.utils.Utilities;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableJpaAuditing()
@EnableCaching
public class SmartTailorBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartTailorBeApplication.class, args);

    }

    @Order(value = 1)
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

    @Order(value = 2)
    @Bean
    public CommandLineRunner createBasicAccount(RoleService roleService,
                                                UserRepository userRepository,
                                                PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findAll().size() == 0) {
                User admin = userRepository.save(User.builder()
                        .email("smarttailor.ad@gmail.com")
                        .password(passwordEncoder.encode("Aa@123456admin"))
                        .phoneNumber("0816468777")
                        .userStatus(UserStatus.ACTIVE)
                        .provider(Provider.LOCAL)
                        .roles(roleService.findRoleByRoleName("ADMIN").orElse(null))
                        .build()
                );

                User manager = userRepository.save(User.builder()
                        .email("smarttailor.ma@gmail.com")
                        .password(passwordEncoder.encode("Aa@123456manager"))
                        .phoneNumber("0877656849")
                        .userStatus(UserStatus.ACTIVE)
                        .provider(Provider.LOCAL)
                        .roles(roleService.findRoleByRoleName("MANAGER").orElse(null))
                        .build()
                );
            }
        };
    }

    @Order(value = 3)
    @Bean
    public CommandLineRunner createExpertTailoring(ExpertTailoringRepository expertTailoringRepository) {
        return args -> {
            if (expertTailoringRepository.findAll().size() == 0) {
                expertTailoringRepository.save(ExpertTailoring
                        .builder()
                        .expertTailoringName("EMBROIDER")
                        .sizeImageUrl("http://img.com")
                        .build()
                );

                expertTailoringRepository.save(ExpertTailoring
                        .builder()
                        .expertTailoringName("SEW")
                        .sizeImageUrl("http://img.com")
                        .build()
                );
            }
        };
    }

    @Order(value = 4)
    @Bean
    public CommandLineRunner createSampleBrand(UserRepository userRepository,
                                               BrandRepository brandRepository,
                                               PasswordEncoder passwordEncoder,
                                               RoleRepository roleRepository,
                                               ExpertTailoringRepository expertTailoringRepository,
                                               BrandExpertTailoringRepository brandExpertTailoringRepository) {
        return args -> {
            if (brandRepository.findAll().size() == 0) {
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

                brandRepository.createShortBrand(userTest1.getUserID(), "LA LA LISA BRAND", BrandStatus.ACCEPT.name());
                brandRepository.createShortBrand(userTest2.getUserID(), "GO YOUN JUNG BRAND", BrandStatus.ACCEPT.name());

                var brandLALALISA = brandRepository.findBrandByBrandName("LA LA LISA BRAND");
                var brandGOYOUNJUNG = brandRepository.findBrandByBrandName("GO YOUN JUNG BRAND");
                var sewExpertTailoring = expertTailoringRepository.findByExpertTailoringName("SEW").get();
                var embroiderExpertTailoring = expertTailoringRepository.findByExpertTailoringName("EMBROIDER").get();
                brandExpertTailoringRepository.createShortBrandExpertTailoring(
                        brandLALALISA.get().getBrandID(),
                        sewExpertTailoring.getExpertTailoringID()
                );

                brandExpertTailoringRepository.createShortBrandExpertTailoring(
                        brandGOYOUNJUNG.get().getBrandID(),
                        embroiderExpertTailoring.getExpertTailoringID()
                );
            }
        };
    }
}
