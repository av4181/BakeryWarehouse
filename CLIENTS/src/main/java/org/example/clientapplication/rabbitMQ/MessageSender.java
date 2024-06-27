package org.example.clientapplication.rabbitMQ;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendOrderMessage(String message) {
        rabbitTemplate.convertAndSend("orderQueue", message);
    }

    public void sendProductMessage(String message) {
        rabbitTemplate.convertAndSend("productQueue", message);
    }

    public void sendUserAccountMessage(String message) {
        rabbitTemplate.convertAndSend("userAccountQueue", message);
    }
}
