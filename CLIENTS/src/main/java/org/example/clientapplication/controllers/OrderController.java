package org.example.clientapplication.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.example.clientapplication.dtos.OrderDTO;
import org.example.clientapplication.dtos.OrderItemDTO;
import org.example.clientapplication.dtos.OrderSummaryDTO;
import org.example.clientapplication.entities.*;
import org.example.clientapplication.enums.OrderStatus;
import org.example.clientapplication.enums.UserType;
import org.example.clientapplication.mappers.OrderItemMapper;
import org.example.clientapplication.mappers.OrderMapper;
import org.example.clientapplication.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {


    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    public OrderController(OrderService orderService, OrderMapper orderMapper, OrderItemMapper orderItemMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
    }

    @PostMapping({"/create"})
    public ResponseEntity<String> createOrder(@RequestBody(required = true) OrderDTO orderDTO) {
        try {
            orderService.createOrder(orderDTO);
            return ResponseEntity.ok("Order created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/copyOrder/{orderId}")
    public ResponseEntity<String> copyOrder(@PathVariable Long orderId) {
        try {
            orderService.copyOrder(orderId);
            return ResponseEntity.ok("Order copied successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/confirm/{orderId}")
    public ResponseEntity<String> confirmOrder(@PathVariable Long orderId) {
        try {
            orderService.confirmOrder(orderId);
            return ResponseEntity.ok("Order confirmed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        try {
            orderService.cancelOrder(orderId);
            return ResponseEntity.ok("Order canceled successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/orderHistory")
    public ResponseEntity<List<OrderSummaryDTO>> getOrdersByUserStatusAndDateRange(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "status", required = false) OrderStatus status,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate) {
        try {
            List<OrderSummaryDTO> orders = orderService.getOrdersByUserStatusAndDateRange(userId, status, startDate, endDate);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @PostMapping("/uploadPurchaseOrder")
    public ResponseEntity<String> uploadPurchaseOrder(@RequestParam("file") MultipartFile
                                                              file, @RequestParam("userId") Long userId) {
        try {
            UserAccount user = orderService.getUserById(userId);
            if(!user.getType().equals(UserType.B2B)){
                return ResponseEntity.badRequest().body("Only B2B users can upload purchase orders");
            }
            PurchaseOrder purchaseOrder = orderService.parsePurchaseOrder(file);
            System.out.println(purchaseOrder);
            Order order = orderService.convertToOrder(purchaseOrder, orderService, user);
            order.setStatus(OrderStatus.CONFIRMED);
            orderService.saveOrder(order);
            return ResponseEntity.ok("Order created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getAllOrders")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        List<OrderDTO> orderDTOs = orders.stream().map(order -> {
            OrderDTO orderDTO = orderMapper.toDto(order);
            List<OrderItemDTO> orderItemDTOs = order.getItems().stream().map(orderItemMapper::toDto).collect(Collectors.toList());
            orderDTO.setItems(orderItemDTOs);
            return orderDTO;
        }).toList();
        return ResponseEntity.ok(orderDTOs);
    }
}