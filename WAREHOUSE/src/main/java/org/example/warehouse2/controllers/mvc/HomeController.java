package org.example.warehouse2.controllers.mvc;

import org.example.warehouse2.controllers.api.dto.IngredientDTO;
import org.example.warehouse2.controllers.api.dto.OrderDTO;
import org.example.warehouse2.controllers.api.dto.SupplierDTO;
import org.example.warehouse2.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

//  Beetje complexere homecontroller om het dashboard overzicht te krijgen telkens je naar home gaat
@Controller
public class HomeController {

    private final IngredientService ingredientService;
    private final SupplierService supplierService;
    private final OrderService orderService;

    @Autowired
    public HomeController(IngredientService ingredientService, SupplierService supplierService, OrderService orderService) {
        this.ingredientService = ingredientService;
        this.supplierService = supplierService;
        this.orderService = orderService;
    }
    private boolean isExpiringSoon(IngredientDTO ingredient, LocalDate expirationThreshold) {
        return ingredient.getExpirationDate() != null && ingredient.getExpirationDate().isBefore(expirationThreshold);
    }

    @GetMapping("/")
    public String home(Model model) {

        List<IngredientDTO> ingredients = ingredientService.getIngredients();
        List<SupplierDTO> suppliers = supplierService.getAllSuppliers();
        List<OrderDTO> orders = orderService.getAllOrders();

        long totalIngredients = ingredients.size();
        long lowStockCount = ingredients.stream()
                .filter(i -> i.getStock() < 10) // TODO stock alert limiet
                .count();
        LocalDate today = LocalDate.now();
        LocalDate expirationThreshold = today.plusWeeks(1); // TODO stock niveau alarm
        List<IngredientDTO> expiringIngredients = ingredientService.getIngredientsExpiringBefore(expirationThreshold);
        long expiringSoonCount = ingredients.stream()
                .filter(i -> isExpiringSoon(i, expirationThreshold))
                .count();

        // Data to model
//        model.addAttribute("pageTitle", "Warehouse Home");
        model.addAttribute("totalIngredients", totalIngredients);
        model.addAttribute("lowStockCount", lowStockCount);
        model.addAttribute("expiringSoonCount", expiringSoonCount);
        model.addAttribute("recentOrders", orders.stream().limit(5).collect(Collectors.toList()));

        return "home"; // tymeleaf home.html
    }
}
