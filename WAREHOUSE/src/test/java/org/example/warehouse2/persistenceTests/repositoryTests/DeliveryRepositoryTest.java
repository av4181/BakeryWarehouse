package org.example.warehouse2.persistenceTests.repositoryTests;

import jakarta.transaction.Transactional;
import org.example.warehouse2.model.*;
import org.example.warehouse2.persistence.repositories.DeliveryRepository;
import org.example.warehouse2.persistence.repositories.IngredientRepository;
import org.example.warehouse2.persistence.repositories.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class DeliveryRepositoryTest {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    private Long deliveryId;

    @BeforeEach
    public void setup() {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order = orderRepository.save(order);

        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientNumber("ING-001");
        ingredient.setName("Flour");
        ingredient.setStock(100);
        ingredient.setStatus(IngredientStatus.AVAILABLE);
        ingredient.setExpirationDate(LocalDate.now().plusMonths(6));
        ingredient = ingredientRepository.save(ingredient);

        DeliveryItem deliveryItem = new DeliveryItem();
        deliveryItem.setIngredientNumber(ingredient.getIngredientNumber());
        deliveryItem.setQuantity(50);

        Delivery delivery = new Delivery();
//        delivery.setMessage("Test delivery");
        delivery.setDestination("Bakery A");
        delivery.setOrderNumber(order.getId());
        delivery.setDeliveryDate(LocalDateTime.now());
        delivery.setStatus(DeliveryStatus.SUCCESS);
        delivery.setItems(List.of(deliveryItem));
        Delivery savedDelivery = deliveryRepository.save(delivery);
        deliveryId = savedDelivery.getId();
    }

    @AfterEach
    public void tearDown() {
        deliveryRepository.deleteAll();
        orderRepository.deleteAll();
        ingredientRepository.deleteAll();
    }

    @Test
    public void testFindByStatus() {
        List<Delivery> deliveries = deliveryRepository.findByStatus(DeliveryStatus.SUCCESS);
        assertFalse(deliveries.isEmpty());
    }

}
