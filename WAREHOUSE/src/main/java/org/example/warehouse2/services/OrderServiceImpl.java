package org.example.warehouse2.services;

import jakarta.transaction.Transactional;
import org.example.warehouse2.config.RabbitMQConfig;
import org.example.warehouse2.controllers.api.dto.OrderDTO;
import org.example.warehouse2.messaging.ProductIngredientsOrderMessage;
import org.example.warehouse2.model.*;
import org.example.warehouse2.model.DeliveryStatus;
import org.example.warehouse2.exceptions.IngredientNotFoundException;
import org.example.warehouse2.exceptions.InsufficientStockException;
import org.example.warehouse2.persistence.mappers.OrderMapper;
import org.example.warehouse2.persistence.repositories.IngredientRepository;
import org.example.warehouse2.persistence.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Delivery createDelivery(ProductIngredientsOrderMessage orderMessage) throws InsufficientStockException, IngredientNotFoundException {
        List<DeliveryItem> deliveryItems = new ArrayList<>();
        for (OrderItem orderItem : orderMessage.getItems()) {
            String ingredientNumber = orderItem.getIngredientNumber();
            int quantity = orderItem.getQuantity();

            // Check voorraad voor een deliveryItem kan aangemaakt worden
            Optional<Ingredient> optionalIngredient = ingredientRepository.findByIngredientNumber(ingredientNumber);
            if (optionalIngredient.isEmpty()) {
                throw new IngredientNotFoundException("Ingredient not found: " + ingredientNumber);
            }

            Ingredient ingredient = optionalIngredient.get();

            if (ingredient.getStock() < quantity) {
                throw new InsufficientStockException("Insufficient stock for ingredient: " + ingredientNumber);
            }

            // Update voorraad na check
            ingredient.setStock(ingredient.getStock() - quantity);
            ingredientRepository.save(ingredient);

            // Aanmaak DeliveryItem instantie
            DeliveryItem deliveryItem = new DeliveryItem(ingredientNumber, quantity);
            deliveryItems.add(deliveryItem);
        }

        // Aanmaak Delivery instantie
        Delivery delivery = new Delivery();
//        delivery.setMessage("Order fulfillment for order " + order.getId());
        //TODO : eventueel extra levering gegevens toevoegen en wat bedoelen we met deliveryDate attribuut ?
        delivery.setDeliveryDate(LocalDateTime.now());
        delivery.setStatus(DeliveryStatus.SUCCESS);
        delivery.setItems(deliveryItems);

        return delivery;
    }

    @Override
    @Transactional
    public DeliveryConfirmation confirmDelivery(Delivery delivery) {

        Order order = orderRepository.findById(delivery.getOrderNumber())
                .orElseThrow(() -> new RuntimeException("Order not found for delivery: " + delivery.getId()));

        // Update stock
        for (DeliveryItem deliveryItem : delivery.getItems()) {
            Optional<Ingredient> optionalIngredient = ingredientRepository.findByIngredientNumber(deliveryItem.getIngredientNumber());
            optionalIngredient.ifPresent(ingredient -> {
                ingredient.setStock(ingredient.getStock() + deliveryItem.getQuantity());
                ingredientRepository.save(ingredient);
            });
        }

        if (delivery.getStatus() == DeliveryStatus.SUCCESS) {
            order.setStatus(OrderStatus.FULFILLED);
        } else {
            order.setStatus(OrderStatus.FAILED);
        }

        orderRepository.save(order);

        return new DeliveryConfirmation(order.getId(), delivery.getStatus());
    }

    // US-49 creëren van een order zodra een message binnenkomt van de bakery
    @Override
    @Transactional
    public OrderDTO createOrderFromMessage(ProductIngredientsOrderMessage message)
            throws IngredientNotFoundException, InsufficientStockException {

        // Create Order entity
        Order order = new Order();
        order.setId(message.getOrderId());
        order.setOrderDate(LocalDateTime.now()); // You might want to use a timestamp from the message
        order.setStatus(OrderStatus.PENDING); // Initial status

        // Process each order item from the message
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItem item : message.getItems()) {
            Ingredient ingredient = ingredientRepository.findByIngredientNumber(item.getIngredientNumber())
                    .orElseThrow(() -> new IngredientNotFoundException("Ingredient not found: " + item.getIngredientNumber()));

            // Stock level check
            if (ingredient.getStock() < item.getQuantity()) {
                throw new InsufficientStockException("Not enough stock for ingredient: " + item.getIngredientNumber());
            }

            // Create OrderItem and associate with Order
            OrderItem orderItem = new OrderItem();
            orderItem.setIngredientNumber(item.getIngredientNumber());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setOrder(order); // Set the order for the OrderItem

            // Update stock
            ingredient.setStock(ingredient.getStock() - item.getQuantity());
            ingredientRepository.save(ingredient);

            orderItems.add(orderItem);
        }

//        order.setItems(orderItems);
        order = orderRepository.save(order);

        return orderMapper.toDTO(order);
    }

    @Override
    public Optional<OrderDTO> getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::toDTO);
    }
}
