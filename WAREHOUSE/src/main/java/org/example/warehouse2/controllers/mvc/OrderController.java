package org.example.warehouse2.controllers.mvc;

import org.example.warehouse2.controllers.api.dto.OrderDTO;
import org.example.warehouse2.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String getAllOrders(Model model) {
        List<OrderDTO> orderDTOs = orderService.getAllOrders();
        model.addAttribute("orders", orderDTOs);
        return "order/order_list";
    }
    @GetMapping("/{id}") // Order detail endpoint
    public String getOrderDetails(@PathVariable("id") UUID orderId, Model model) {
        Optional<OrderDTO> orderDTOOptional = orderService.getOrderById(orderId);
        if (orderDTOOptional.isPresent()) {
            model.addAttribute("orderDTO", orderDTOOptional.get());
            return "order/order_details";
        } else {
            model.addAttribute("error", "Order not found");
            return "error_page";
        }
    }
}
