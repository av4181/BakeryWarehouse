package org.example.warehouse2.controllers.mvc;

import org.example.warehouse2.controllers.api.dto.IngredientDTO;
import org.example.warehouse2.exceptions.IngredientNotFoundException;
import org.example.warehouse2.model.Ingredient;
import org.example.warehouse2.persistence.mappers.IngredientMapper;
import org.example.warehouse2.services.IngredientService;
import org.example.warehouse2.services.SupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ingredients")
public class IngredientController {

    private final Logger logger = LoggerFactory.getLogger(IngredientController.class);
    private final IngredientService ingredientService;
    private final IngredientMapper ingredientMapper;
    private final SupplierService supplierService;

    @Autowired
    public IngredientController(IngredientService ingredientService, IngredientMapper ingredientMapper, SupplierService supplierService) {
        this.ingredientService = ingredientService;
        this.ingredientMapper = ingredientMapper;
        this.supplierService = supplierService;
    }

    @GetMapping
    public String getAllIngredients(Model model) {
        List<IngredientDTO> ingredients = Optional.ofNullable(ingredientService.getIngredients())
                .orElse(Collections.emptyList());

        model.addAttribute("ingredients", ingredients);
        return "ingredient/ingredient_list";
    }

    @GetMapping("/{ingredientNumber}")
    public String showIngredient(@PathVariable String ingredientNumber, Model model) {
        try {
            Ingredient ingredient = ingredientService.getIngredientByNumber(ingredientNumber);
            IngredientDTO ingredientDTO = ingredientMapper.toDTO(ingredient);
            model.addAttribute("ingredient", ingredientDTO);
            return "ingredient/ingredient_details";
        } catch (IngredientNotFoundException e) {
            logger.warn("Ingredient not found for ingredientNumber: {}", ingredientNumber);
            model.addAttribute("error", "Ingredient not found");
            return "error";
        }
    }

    //US-57 lijst bijna vervallen ingredienten opvragen
    @GetMapping("/expiring")
    public String getExpiringIngredients(Model model) {
        LocalDate expirationDate = LocalDate.now().plusWeeks(1); // 1 week limiet
        List<IngredientDTO> expiringIngredients = Optional.ofNullable(ingredientService.getIngredientsExpiringBefore(expirationDate))
                .orElse(Collections.emptyList());
        model.addAttribute("expiringIngredients", expiringIngredients);
        return "ingredient/ingredients_expiring";
    }
}
