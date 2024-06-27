package org.example.warehouse2;

import org.example.warehouse2.messaging.InventoryUpdateMessage;
import org.example.warehouse2.messaging.MessageSender;
import org.example.warehouse2.services.CacheEvictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.Scanner;

@SpringBootApplication
public class Warehouse2Application {

    @Autowired
    private MessageSender messageSender;
    @Bean
    public CommandLineRunner clearCache(CacheEvictionService cacheEvictionService) {
        return args -> cacheEvictionService.evictAllCaches();
    }

    public static void main(String[] args) {
        SpringApplication.run(Warehouse2Application.class, args);

    }

//    public static void main(String[] args) {
//        ConfigurableApplicationContext context = SpringApplication.run(Warehouse2Application.class, args);
//
//        MessageSender messageSender = context.getBean(MessageSender.class);
//
//        InventoryUpdateMessage message = new InventoryUpdateMessage("ING-001", 120);
//        messageSender.sendInventoryUpdateMessage(message);
//        System.out.println("Message Sent! (Press Enter to exit)");
//
//        Scanner scanner = new Scanner(System.in);
//        scanner.nextLine();
//
//        context.close();
//    }

    // TODO
}
