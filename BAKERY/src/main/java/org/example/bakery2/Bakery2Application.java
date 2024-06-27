package org.example.bakery2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Bakery2Application {

    public static void main(String[] args) {
        SpringApplication.run(Bakery2Application.class, args);
    }

}
