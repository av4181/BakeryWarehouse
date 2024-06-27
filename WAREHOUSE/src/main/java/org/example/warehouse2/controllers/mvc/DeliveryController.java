package org.example.warehouse2.controllers.mvc;

import org.example.warehouse2.model.Delivery;
import org.example.warehouse2.model.DeliveryConfirmation;
import org.example.warehouse2.services.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/delivery")
public class DeliveryController {

    private final OrderService orderService;

    public DeliveryController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{deliveryId}/confirm")
    public String confirmDelivery(@PathVariable Long deliveryId, @ModelAttribute Delivery delivery, RedirectAttributes redirectAttributes) {

        try {
            DeliveryConfirmation confirmation = orderService.confirmDelivery(delivery);
            redirectAttributes.addFlashAttribute("message", "Delivery confirmed successfully!");
            return "redirect:/delivery";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to confirm delivery: " + e.getMessage());
            return "redirect:/delivery/" + deliveryId + "/confirm";
        }
    }
}
