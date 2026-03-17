package com.walterb0b.sprataskastereoverblik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpratasKastereOverblikApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpratasKastereOverblikApplication.class, args);
    }

}
