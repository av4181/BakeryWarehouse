package org.example.warehouse2.serviceTests.mockito;

import org.example.warehouse2.config.RabbitMQConfig;
import org.example.warehouse2.messaging.InventoryUpdateMessage;
import org.example.warehouse2.model.Ingredient;
import org.example.warehouse2.persistence.repositories.IngredientRepository;
import org.example.warehouse2.serviceTests.mockito.fixtures.StockFixtures;
import org.example.warehouse2.services.StockServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class StockServiceImplTest {

    @Mock
    private IngredientRepository ingredientRepository;
    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private StockServiceImpl stockService;

    @Test
    void testUpdateStock_IngredientExists() {
        Ingredient existingIngredient = StockFixtures.createIngredientWithStock("ING-001", 20);
        int stockCount = 10;

        Mockito.lenient().when(ingredientRepository.findByIngredientNumber(existingIngredient.getIngredientNumber()))
                .thenReturn(Optional.of(existingIngredient));

        String result = stockService.updateStock(existingIngredient.getIngredientNumber(), stockCount);

        assertEquals("Stock for ingredient ING-001 updated successfully", result);
        assertEquals(30, existingIngredient.getStock());
        verify(ingredientRepository).save(existingIngredient);

        // Check versturen bericht naar RabbitMQ
        InventoryUpdateMessage expectedMessage = new InventoryUpdateMessage(existingIngredient.getIngredientNumber(), 30);
        verify(rabbitTemplate).convertAndSend(
                RabbitMQConfig.WAREHOUSE_EXCHANGE,
                RabbitMQConfig.INVENTORY_UPDATE_ROUTING_KEY,
                expectedMessage
        );
    }

    @Test
    void testUpdateStock_IngredientNotFound() {
        String ingredientNumber = "ING-002"; // ONBESTAAND INGREDIENT
        int stockCount = 10;

        Mockito.lenient().when(ingredientRepository.findByIngredientNumber(ingredientNumber)).thenReturn(Optional.empty());

        String result = stockService.updateStock(ingredientNumber, stockCount);

        assertEquals("Ingredient not found: " + ingredientNumber, result);
        verify(ingredientRepository, never()).save(any());
        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), any(InventoryUpdateMessage.class));
    }
}
