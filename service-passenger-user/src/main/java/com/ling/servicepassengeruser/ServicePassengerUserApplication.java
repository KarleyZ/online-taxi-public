package com.ling.servicepassengeruser;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@MapperScan("com.ling.servicepassengeruser.mapper")
@EnableDiscoveryClient
public class ServicePassengerUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicePassengerUserApplication.class);
    }
}
