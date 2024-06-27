package org.example.warehouse2.messaging;

import jakarta.transaction.Transactional;
import org.example.warehouse2.config.RabbitMQConfig;
import org.example.warehouse2.exceptions.IngredientNotFoundException;
import org.example.warehouse2.model.Ingredient;
import org.example.warehouse2.services.IngredientService;
import org.example.warehouse2.services.IngredientServiceImpl;
import org.example.warehouse2.services.OrderServiceImpl;
import org.example.warehouse2.services.StockServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InventoryUpdateMessageListener {

    private final Logger logger = LoggerFactory.getLogger(InventoryUpdateMessageListener.class);
    private final StockServiceImpl stockService;

    // Messaging werkt op controller niveau, je hebt dus steeds een service nodig om iets met de message
    // te doen
    private final OrderServiceImpl orderService;

    @Autowired
    public InventoryUpdateMessageListener(StockServiceImpl stockService, OrderServiceImpl orderService) {
        this.stockService = stockService;
        this.orderService = orderService;
    }

    //US-55 automatisch bijbestellen van een ingredient
    @RabbitListener(queues = RabbitMQConfig.INVENTORY_UPDATE_QUEUE)
    @Transactional
    public void handleInventoryUpdate(InventoryUpdateMessage message) throws IngredientNotFoundException {
        logger.info("Received inventory update message: {}", message);

        String ingredientNumber = message.getIngredientNumber();
        int newStockLevel = message.getNewStockLevel();


        stockService.updateStock(ingredientNumber, newStockLevel);

        // Check stock niveau
        if (newStockLevel < RabbitMQConfig.REORDER_THRESHOLD) {
            logger.info("Ingredient '{}' is below reorder threshold. Triggering order...", ingredientNumber);
            stockService.placeReorderForIngredient(ingredientNumber);
        }

        logger.info("Ingredient stock updated successfully: {} - New Stock Level: {}", ingredientNumber, newStockLevel);
    }
}
