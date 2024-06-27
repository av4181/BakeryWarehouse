package org.example.clientapplication.rabbitMQ;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue orderQueue() {
        return new Queue("orderQueue", false);
    }

    @Bean
    public Queue productQueue() {
        return new Queue("productQueue", false);
    }

    @Bean
    public Queue userAccountQueue() {
        return new Queue("userAccountQueue", false);
    }

    @Bean Queue newProductQueue() {
        return new Queue("new-product-queue", false);
    }
}
