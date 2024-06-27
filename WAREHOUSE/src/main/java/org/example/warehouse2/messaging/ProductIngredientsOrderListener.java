package org.example.warehouse2.messaging;

import org.example.warehouse2.controllers.api.dto.OrderDTO;
import org.example.warehouse2.exceptions.IngredientNotFoundException;
import org.example.warehouse2.exceptions.InsufficientStockException;
import org.example.warehouse2.model.Delivery;
import org.example.warehouse2.model.OrderStatus;
import org.example.warehouse2.services.OrderService;
import org.example.warehouse2.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// US-49 Listener ontvangt de message van de queue en roept createOrderFromMessage() aan om een Order te maken
// van de message
@Component
public class ProductIngredientsOrderListener {

    private static final Logger logger = LoggerFactory.getLogger(ProductIngredientsOrderListener.class);
    // Messaging werkt op controller niveau, je hebt dus steeds een service nodig om iets met de message
    // te doen
    private final OrderService orderService;

    @Autowired
    public ProductIngredientsOrderListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = RabbitMQConfig.PRODUCT_INGREDIENTS_ORDER_QUEUE)
    public void handleProductIngredientsOrder(ProductIngredientsOrderMessage message) {
        logger.info("Received product ingredients order message: {}", message);

        try {
            OrderDTO orderDTO = orderService.createOrderFromMessage(message);
            Delivery delivery = orderService.createDelivery(message);
            orderService.confirmDelivery(delivery);
        } catch (IngredientNotFoundException | InsufficientStockException e) {
            return;
        };
    }
}

