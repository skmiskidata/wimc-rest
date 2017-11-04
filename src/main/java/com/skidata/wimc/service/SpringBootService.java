package com.skidata.wimc.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan(basePackages = {
        "com.skidata.wimc",
        "com.skidata.wimc.service",
        "com.skidata.wimc.sighthound.client"})
public class SpringBootService {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootService.class, args);
    }

}


