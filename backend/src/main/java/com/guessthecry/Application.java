package com.guessthecry;

import com.guessthecry.config.S3Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        S3Config.envCheck();
        SpringApplication.run(Application.class, args);
    }
}