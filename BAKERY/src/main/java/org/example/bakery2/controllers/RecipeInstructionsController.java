package org.example.bakery2.controllers;

import org.example.bakery2.entities.Recipe;
import org.example.bakery2.entities.commands.recipe.UpdateRecipeInstructionsCommand;
import org.example.bakery2.services.RecipeInstructionService;
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
@RequestMapping("/recipeInstructions")
public class RecipeInstructionsController {

    private static final Logger logger = LoggerFactory.getLogger(RecipeInstructionsController.class);

    private final RecipeInstructionService recipeInstructionService;

    public RecipeInstructionsController(RecipeInstructionService recipeInstructionService) {
        this.recipeInstructionService = recipeInstructionService;
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<Recipe> updateRecipeInstructions(@PathVariable Long recipeId, @RequestBody UpdateRecipeInstructionsCommand updateRecipeInstructionsCommand) {
        logger.info("Updating instructions for recipe with ID: {}", recipeId);
        Recipe updatedRecipe = recipeInstructionService.updateRecipeInstructions(recipeId, updateRecipeInstructionsCommand);
        logger.info("Instructions for recipe with ID: {} updated successfully", recipeId);
        return ResponseEntity.ok(updatedRecipe);
    }

    //REMOVE INSTRUCTIONS FROM RECIPE
    @DeleteMapping("/{recipeId}")
    public ResponseEntity<Void> deleteRecipeInstructionsId(@PathVariable Long recipeId) {
        logger.info("Removing instructions from recipe with ID: {}", recipeId);
        recipeInstructionService.removeInstructionsFromRecipe(recipeId);
        logger.info("Instructions from recipe with ID: {} removed successfully", recipeId);
        return ResponseEntity.noContent().build();
    }

}
