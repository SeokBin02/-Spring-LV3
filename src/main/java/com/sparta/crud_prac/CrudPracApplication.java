package com.sparta.crud_prac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CrudPracApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrudPracApplication.class, args);
    }

}
