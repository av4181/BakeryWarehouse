package org.example.bakery2.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.bakery2.entities.Ingredient;
import org.example.bakery2.entities.commands.ingredient.CreateIngredientCommand;
import org.example.bakery2.services.IngredientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Controller
@RequestMapping("/ingredients")
public class IngredientController {

    private static final Logger logger = LoggerFactory.getLogger(IngredientController.class);

    private final IngredientService ingredientService;

    private IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @RequestMapping("/list")
    public String ingredientsList(Model model) {
        logger.info("Listing all ingredients");
        model.addAttribute("ingredients", ingredientService.findAllIngredients());
        logger.info("Ingredients listed successfully");
        return "ingredient/ingredients";
    }

    @RequestMapping("/create")
    public String showCreateIngredientForm(Model model) {
        logger.info("Displaying form to create a new ingredient");
        model.addAttribute("createIngredientCommand", new CreateIngredientCommand(""));
        logger.info("Create ingredient form displayed");
        return "ingredient/createIngredient";
    }

    @PostMapping("/ingredients")
    public String createIngredient(CreateIngredientCommand createIngredientCommand, HttpServletRequest request) {
        logger.info("Creating new ingredient: {}", createIngredientCommand.name());
        ingredientService.createIngredient(createIngredientCommand);
        logger.info("New ingredient created: {}", createIngredientCommand.name());
        return "redirect:" + request.getHeader("referer");
    }

    //TODO add check if not in recipe!
    @DeleteMapping("/{ingredientId}")
    public ResponseEntity<Void> deleteIngredientId(@PathVariable Long ingredientId) {
        logger.info("Deleting ingredient with ID: {}", ingredientId);
        ingredientService.deleteIngredientById(ingredientId);
        logger.info("Ingredient deleted with ID: {}", ingredientId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping("/lowStock")
    public String getLowStockIngredients(Model model) {
        RestTemplate restTemplate = new RestTemplate();
        //TODO CHANGE LOCALHOST URL TO CORRECT API ENDPOINT FROM WAREHOUSE
        Ingredient[] ingredientsArray = restTemplate.getForObject("http://localhost:8081/lowstockIngredients", Ingredient[].class);
        model.addAttribute("lowStockIngredients", Arrays.asList(ingredientsArray));
        return "ingredient/lowStockIngredients";
    }

}
