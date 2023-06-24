package com.ict;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class GuacamoleWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuacamoleWebApplication.class, args);
    }

}
