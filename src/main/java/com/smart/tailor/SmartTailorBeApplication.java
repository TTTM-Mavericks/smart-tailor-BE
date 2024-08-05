package com.smart.tailor;

import com.smart.tailor.entities.User;
import com.smart.tailor.enums.Provider;
import com.smart.tailor.enums.UserStatus;
import com.smart.tailor.repository.UserRepository;
import com.smart.tailor.service.RoleService;
import com.smart.tailor.utils.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootApplication
@EnableJpaAuditing()
@EnableCaching
@EnableScheduling
public class SmartTailorBeApplication {
    public static final Logger logger = LoggerFactory.getLogger(SmartTailorBeApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SmartTailorBeApplication.class, args);
    }

//    @Order(value = 1)
//    @Bean
//    public CommandLineRunner runScript(DataSource dataSource) throws Exception {
//        return args -> {
//            try (Connection connection = dataSource.getConnection()) {
//                if (!isSchemaAlreadyInitialized(connection)) {
//                    ScriptUtils.executeSqlScript(connection, new ClassPathResource("smartTailorScript.sql"));
//                    System.out.println("SCRIPT IS RUNNING");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        };
//    }
//
//    private boolean isSchemaAlreadyInitialized(Connection connection) throws SQLException {
//        try (Statement statement = connection.createStatement()) {
//            // Kiểm tra sự tồn tại của bảng schema_version
//            ResultSet resultSet = statement.executeQuery(
//                    "SELECT COUNT(*) " +
//                            "FROM smart_tailor_be.roles");
//            if (resultSet.next()) {
//                int count = resultSet.getInt(1);
//                return count > 0;
//            } else {
//                return false;
//            }
//        }
//    }

//    @Order(value = 2)
//    @Bean
//    public CommandLineRunner createBasicAccount(RoleService roleService,
//                                                UserRepository userRepository,
//                                                PasswordEncoder passwordEncoder) {
//        return args -> {
//            if (userRepository.findAll().size() == 0) {
//                User admin = userRepository.save(User.builder()
//                        .email("smarttailor.ad@gmail.com")
//                        .password(passwordEncoder.encode("Aa@123456admin"))
//                        .phoneNumber("0816468777")
//                        .userStatus(UserStatus.ACTIVE)
//                        .provider(Provider.LOCAL)
//                        .roles(roleService.findRoleByRoleName("ADMIN").get())
//                        .build()
//                );
//
//                User manager = userRepository.save(User.builder()
//                        .email("smarttailor.ma@gmail.com")
//                        .password(passwordEncoder.encode("Aa@123456manager"))
//                        .phoneNumber("0877656849")
//                        .userStatus(UserStatus.ACTIVE)
//                        .provider(Provider.LOCAL)
//                        .roles(roleService.findRoleByRoleName("MANAGER").get())
//                        .build()
//                );
//            }
//        };
//    }
}
