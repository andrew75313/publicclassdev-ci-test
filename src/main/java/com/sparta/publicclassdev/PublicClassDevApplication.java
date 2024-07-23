package com.sparta.publicclassdev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableCaching
public class PublicClassDevApplication {

    public static void main(String[] args) {
        SpringApplication.run(PublicClassDevApplication.class, args);
    }

}
