package com.restful.quanlysinhvien;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// disable security
// @SpringBootApplication(exclude = {
// org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
// org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class
// })
public class QuanlysinhvienApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuanlysinhvienApplication.class, args);
    }

}
