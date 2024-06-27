package org.example.bakery2.controllers;

import org.example.bakery2.entities.Product;
import org.example.bakery2.services.BakingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("")
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    private final BakingService bakingService;

    public HomeController(BakingService bakingService) {
        this.bakingService = bakingService;
    }

    @RequestMapping("/")
    public String home(Model model) {
        logger.info("Home page accessed");
        logger.info("Listing all products to the baked list");
        Map<Product, Integer> productsToBeBaked = bakingService.getProductsToBeBaked();
        model.addAttribute("productsToBeBaked", productsToBeBaked);
        return "home";
    }

}
