package com.neoprep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class NeoPrepApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeoPrepApplication.class, args);
    }
}
