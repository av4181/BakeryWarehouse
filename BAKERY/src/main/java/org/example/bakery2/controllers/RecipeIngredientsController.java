package org.example.bakery2.controllers;

import org.example.bakery2.entities.RecipeIngredient;
import org.example.bakery2.entities.commands.recipeIngredient.UpdateRecipeIngredientCommand;
import org.example.bakery2.services.RecipeIngredientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recipeIngredients")
public class RecipeIngredientsController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final RecipeIngredientService recipeIngredientService;

    public RecipeIngredientsController(RecipeIngredientService recipeIngredientService) {
        this.recipeIngredientService = recipeIngredientService;
    }

    @PutMapping("/{recipeIngredientId}")
    public ResponseEntity<RecipeIngredient> updateRecipeIngredient(@PathVariable Long recipeIngredientId, @RequestBody UpdateRecipeIngredientCommand updateRecipeIngredientCommand) {
        logger.info("Updating recipe ingredient with ID: {}", recipeIngredientId);
        RecipeIngredient updatedRecipeIngredient = recipeIngredientService.updateRecipeIngredient(recipeIngredientId, updateRecipeIngredientCommand);
        logger.info("Recipe ingredient with ID: {} updated successfully", recipeIngredientId);
        return ResponseEntity.ok(updatedRecipeIngredient);
    }

    //REMOVE INGREDIENTS FROM RECIPE
    @DeleteMapping("/{recipeIngredientId}")
    public ResponseEntity<Void> deleteRecipeIngredientId(@PathVariable Long recipeIngredientId) {
        logger.info("Removing recipe ingredient with ID: {}", recipeIngredientId);
        recipeIngredientService.removeRecipeIngredientByIdFromRecipe(recipeIngredientId);
        logger.info("Recipe ingredient with ID: {} removed successfully", recipeIngredientId);
        return ResponseEntity.noContent().build();
    }

}
