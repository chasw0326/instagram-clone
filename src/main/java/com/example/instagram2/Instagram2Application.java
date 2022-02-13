package com.example.instagram2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Instagram2Application {

    public static void main(String[] args) {
        SpringApplication.run(Instagram2Application.class, args);
    }

}
