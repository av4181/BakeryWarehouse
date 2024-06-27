package org.example.bakery2.services;

import org.example.bakery2.entities.RecipeIngredient;
import org.example.bakery2.entities.commands.recipeIngredient.UpdateRecipeIngredientCommand;
import org.example.bakery2.exceptions.RecipeFinalizedException;
import org.example.bakery2.exceptions.RecipeIngredientCommandNotFoundException;
import org.example.bakery2.exceptions.RecipeIngredientNotFoundException;
import org.example.bakery2.exceptions.RecipeNotFoundException;
import org.example.bakery2.mappers.RecipeIngredientMapper;
import org.example.bakery2.repositories.RecipeIngredientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.InputMismatchException;
import java.util.Optional;

@Service
public class RecipeIngredientService {

    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeIngredientMapper recipeIngredientMapper;


    public RecipeIngredientService(RecipeIngredientRepository recipeIngredientRepository, RecipeIngredientMapper recipeIngredientMapper) {
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeIngredientMapper = recipeIngredientMapper;
    }

    @Transactional
    public void removeRecipeIngredientByIdFromRecipe(Long recipeIngredientId) {
        Optional<RecipeIngredient> maybeRecipeIngredient = recipeIngredientRepository.findById(recipeIngredientId);
        if (maybeRecipeIngredient.isEmpty())
            throw new RecipeIngredientNotFoundException(String.format("No recipeIngredient with id %d found", recipeIngredientId));

        if (maybeRecipeIngredient.get().getRecipe() == null) {
            throw new RecipeNotFoundException(String.format("No recipe found for recipe ingredient with id %d", recipeIngredientId));
        }

        if (maybeRecipeIngredient.get().getRecipe().isFinalized()) {
            throw new RecipeFinalizedException(String.format("Recipe with id %d is finalized and cannot be modified", maybeRecipeIngredient.get().getRecipe().getId()));
        }

        maybeRecipeIngredient.get().setRecipe(null);
        recipeIngredientRepository.save(maybeRecipeIngredient.get());
    }

    @Transactional
    public RecipeIngredient updateRecipeIngredient(Long recipeIngredientId, UpdateRecipeIngredientCommand updateRecipeIngredientCommand) {
        if (updateRecipeIngredientCommand == null) {
            throw new RecipeIngredientCommandNotFoundException("Update ingredientRecipe command not found");
        }

        Optional<RecipeIngredient> maybeRecipeIngredient = recipeIngredientRepository.findById(recipeIngredientId);
        if (maybeRecipeIngredient.isEmpty())
            throw new RecipeIngredientNotFoundException(String.format("No recipeIngredient with Id %d found", recipeIngredientId));

        if (maybeRecipeIngredient.get().getRecipe().isFinalized()) {
            throw new RecipeFinalizedException(String.format("Recipe with id %d is finalized and cannot be modified", maybeRecipeIngredient.get().getRecipe().getId()));
        }

        if (updateRecipeIngredientCommand.amount() <= 0) {
            throw new InputMismatchException(String.format("Recipe with id %d cant be less or equal than 0", recipeIngredientId));
        }
        //TODO add checks for parsing strings as doubles + frontend validation

        RecipeIngredient recipeIngredient = recipeIngredientMapper.modifyEntity(maybeRecipeIngredient.get(), updateRecipeIngredientCommand);

        RecipeIngredient updatedRecipeIngredient = recipeIngredientRepository.save(recipeIngredient);

        return updatedRecipeIngredient;
    }
}
