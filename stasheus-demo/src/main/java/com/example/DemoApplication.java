package com.example;

import io.stasheus.Stasheus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        new Stasheus.Builder()
                .hostname("localhost")
                .port(8080)
                .endpoint("/actuator/prometheus")
                .rate(10)
                .build();

        SpringApplication.run(DemoApplication.class, args);
    }
}
