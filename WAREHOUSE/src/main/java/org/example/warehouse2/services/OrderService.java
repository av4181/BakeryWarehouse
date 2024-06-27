package org.example.warehouse2.services;

import org.example.warehouse2.controllers.api.dto.NewOrderDTO;
import org.example.warehouse2.controllers.api.dto.OrderDTO;
import org.example.warehouse2.exceptions.IngredientNotFoundException;
import org.example.warehouse2.exceptions.InsufficientStockException;
import org.example.warehouse2.exceptions.OrderNotFoundException;
import org.example.warehouse2.messaging.ProductIngredientsOrderMessage;
import org.example.warehouse2.model.Delivery;
import org.example.warehouse2.model.DeliveryConfirmation;
import org.example.warehouse2.model.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {
    List<OrderDTO> getAllOrders();
    Delivery createDelivery(ProductIngredientsOrderMessage orderMessage) throws InsufficientStockException, IngredientNotFoundException;
    DeliveryConfirmation confirmDelivery(Delivery delivery);
    OrderDTO createOrderFromMessage(ProductIngredientsOrderMessage message) throws InsufficientStockException, IngredientNotFoundException;
    Optional<OrderDTO> getOrderById(UUID orderId);
}
