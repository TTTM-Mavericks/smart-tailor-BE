package com.smart.tailor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing()
@EnableCaching
@EnableScheduling
public class SmartTailorBeApplication {
    public static final Logger logger = LoggerFactory.getLogger(SmartTailorBeApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SmartTailorBeApplication.class, args);
    }
}
