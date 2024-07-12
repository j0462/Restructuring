package com.sparta.restructuring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RestructuringApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestructuringApplication.class, args);
    }

}
