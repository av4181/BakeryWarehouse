package org.example.bakery2.rabbitMQ.sender;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;

public class MessageSender {

    public static final String NEW_PRODUCT_QUEUE = "new-product-queue";
    public static final String PRODUCT_BAKED = "product-baked-queue";
    public static final String PRODUCT_ACTIVE_STATE_CHANGED = "product-active-state-changed-queue";
    public static final String PRODUCT_INGREDIENTS_ORDER = "product-ingredients-order-queue";

    @Bean
    public Queue newProductQueue() {
        return new Queue(NEW_PRODUCT_QUEUE, false);
    }

    @Bean
    public Queue productBakedQueue() {
        return new Queue(PRODUCT_BAKED, false);
    }

    @Bean
    public Queue productActiveStateChangedQueue() {
        return new Queue(PRODUCT_ACTIVE_STATE_CHANGED, false);
    }

    @Bean
    public Queue productIngredientsOrderQueue() {
        return new Queue(PRODUCT_INGREDIENTS_ORDER, false);
    }

}
