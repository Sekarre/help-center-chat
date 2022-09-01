package com.sekarre.helpcenterchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class HelpCenterChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelpCenterChatApplication.class, args);
    }
}
