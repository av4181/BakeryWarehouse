package org.example.warehouse2.services;
import org.example.warehouse2.exceptions.IngredientNotFoundException;

import jakarta.transaction.Transactional;
import org.example.warehouse2.config.RabbitMQConfig;
import org.example.warehouse2.exceptions.IngredientNotFoundException;
import org.example.warehouse2.exceptions.SupplierNotFoundException;
import org.example.warehouse2.messaging.InventoryUpdateMessage;
import org.example.warehouse2.model.Ingredient;
import org.example.warehouse2.model.Supplier;
import org.example.warehouse2.persistence.repositories.IngredientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class StockServiceImpl implements StockService {

    private static final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);

    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public String updateStock(String ingredientNumber, int stockCount) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findByIngredientNumber(ingredientNumber);
        if (optionalIngredient.isEmpty()) {
            return "Ingredient not found: " + ingredientNumber;
        }

        Ingredient ingredient = optionalIngredient.get();
        ingredient.setStock(ingredient.getStock() + stockCount);
        ingredientRepository.save(ingredient);

        // RabbitMQ message wanneer stock is geupdated
        InventoryUpdateMessage message = new InventoryUpdateMessage(ingredientNumber, ingredient.getStock());
        rabbitTemplate.convertAndSend(RabbitMQConfig.WAREHOUSE_EXCHANGE, RabbitMQConfig.INVENTORY_UPDATE_ROUTING_KEY, message);
        logger.info("Sent InventoryUpdateMessage: {}", message);

        return "Stock for ingredient " + ingredientNumber + " updated successfully";
    }

    @Override
    @Transactional
    public void placeReorderForIngredient(String ingredientNumber) throws IngredientNotFoundException, SupplierNotFoundException {
        // US-55 Ingredient
        Ingredient ingredient = ingredientRepository.findByIngredientNumber(ingredientNumber)
                .orElseThrow(() -> new IngredientNotFoundException("Ingredient not found: " + ingredientNumber));

        // Check stock
        if (ingredient.getStock() >= RabbitMQConfig.REORDER_THRESHOLD) {
            return;
        }

        Supplier supplier = ingredient.getSupplier();

        int quantityToOrder = RabbitMQConfig.REORDER_AMOUNT - ingredient.getStock();

        // Update ingredient stock
        ingredient.setStock(ingredient.getStock() + quantityToOrder);
        ingredientRepository.save(ingredient);

        // Send an InventoryUpdateMessage to indicate the stock has been "virtually" updated
        InventoryUpdateMessage inventoryUpdateMessage = new InventoryUpdateMessage(ingredientNumber, ingredient.getStock());
        rabbitTemplate.convertAndSend(RabbitMQConfig.WAREHOUSE_EXCHANGE, RabbitMQConfig.INVENTORY_UPDATE_ROUTING_KEY, inventoryUpdateMessage);
    }
}
