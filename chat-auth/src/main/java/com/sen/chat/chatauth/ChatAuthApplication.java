package com.sen.chat.chatauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class ChatAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatAuthApplication.class, args);
    }

}
