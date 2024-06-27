package org.example.clientapplication.rabbitMQ;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserAccountMessageListener {
    @RabbitListener(queues = "userAccountQueue")
    public void handleUserAccountMessage(String message) {
        System.out.println("Received user account message: " + message);
    }
}
