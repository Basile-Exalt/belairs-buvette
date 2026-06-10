package com.it.exalt.belair.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.it.exalt.belair")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
