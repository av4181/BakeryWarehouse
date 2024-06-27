package org.example.warehouse2.messaging;

import org.example.warehouse2.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {

    private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendInventoryUpdateMessage(InventoryUpdateMessage message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.WAREHOUSE_EXCHANGE, RabbitMQConfig.INVENTORY_UPDATE_ROUTING_KEY, message);
        logger.info("Sent InventoryUpdateMessage: {}", message);
    }

    public void sendOrderCreatedMessage(ProductIngredientsOrderMessage message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.WAREHOUSE_EXCHANGE, RabbitMQConfig.PRODUCT_INGREDIENTS_ORDER_ROUTING_KEY, message);
        logger.info("Sent OrderPlacedEvent: {}", message);
    }

    // TODO
}
