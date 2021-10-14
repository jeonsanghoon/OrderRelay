package com.them.orderrelay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ServletComponentScan
@SpringBootConfiguration
@EnableAsync
public class OrderRelayApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderRelayApplication.class, args);
    }
}
