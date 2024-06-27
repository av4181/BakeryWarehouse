package org.example.clientapplication.rabbitMQ;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderMessageListener {
    @RabbitListener(queues = "orderQueue")
    public void handleOrderMessage(String message) {
        System.out.println("Received order message: " + message);
    }
}
