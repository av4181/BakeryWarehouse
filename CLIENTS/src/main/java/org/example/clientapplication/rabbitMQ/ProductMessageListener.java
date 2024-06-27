package org.example.clientapplication.rabbitMQ;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ProductMessageListener {
    @RabbitListener(queues = "productQueue")
    public void handleProductMessage(String message) {
        System.out.println("Received product message: " + message);
    }
}
