package com.heqingbao.microweather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class WeatherConfigClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherConfigClientApplication.class, args);
    }

}

