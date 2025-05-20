package com.kdev5.cleanpick;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CleanpickApplication {

    public static void main(String[] args) {
        SpringApplication.run(CleanpickApplication.class, args);
    }

}
