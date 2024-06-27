package org.example.bakery2.services;

import org.example.bakery2.entities.Recipe;
import org.example.bakery2.entities.commands.recipe.AddInstructionsToRecipeCommand;
import org.example.bakery2.entities.commands.recipe.UpdateRecipeInstructionsCommand;
import org.example.bakery2.exceptions.AddInstructionsToRecipeCommandNotFoundException;
import org.example.bakery2.exceptions.IncompleteException;
import org.example.bakery2.exceptions.RecipeFinalizedException;
import org.example.bakery2.exceptions.RecipeNotFoundException;
import org.example.bakery2.exceptions.UpdateRecipeInstructionsCommandNotFoundException;
import org.example.bakery2.repositories.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RecipeInstructionService {

    private final RecipeRepository recipeRepository;

    public RecipeInstructionService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Transactional
    public void addInstructionsToRecipe(Long recipeId, AddInstructionsToRecipeCommand addInstructionsToRecipeCommand) {
        if (addInstructionsToRecipeCommand == null)
            throw new AddInstructionsToRecipeCommandNotFoundException("Add instructions to recipe command not found");

        Optional<Recipe> maybeRecipe = recipeRepository.findById(recipeId);
        if (maybeRecipe.isEmpty())
            throw new RecipeNotFoundException(String.format("No recipe with id %d found", recipeId));

        if (addInstructionsToRecipeCommand.instructions().isEmpty())
            throw new IncompleteException("The instructions that were provided are incomplete. Instructions should not be empty or null");

        Recipe recipeToBeUpdated = maybeRecipe.get();
        if (recipeToBeUpdated.isFinalized()) {
            throw new RecipeFinalizedException(String.format("Recipe with id %d is finalized and cannot be modified", recipeId));
        }

        recipeToBeUpdated.setInstructions(addInstructionsToRecipeCommand.instructions());
        Recipe recipeWithInstructions = recipeRepository.save(recipeToBeUpdated);

    }

    @Transactional
    public Recipe updateRecipeInstructions(Long recipeId, UpdateRecipeInstructionsCommand updateRecipeInstructionsCommand) {
        if (updateRecipeInstructionsCommand == null) {
            throw new UpdateRecipeInstructionsCommandNotFoundException("Update recipeInstructions command not found");
        }

        Optional<Recipe> maybeRecipe = recipeRepository.findById(recipeId);
        if (maybeRecipe.isEmpty())
            throw new RecipeNotFoundException(String.format("No recipe with Id %d found", recipeId));

        if (updateRecipeInstructionsCommand.instructions().isEmpty())
            throw new IncompleteException("The instructions that were provided are incomplete. Instructions should not be empty or null");

        if (maybeRecipe.get().isFinalized()) {
            throw new RecipeFinalizedException(String.format("Recipe with id %d is finalized and cannot be modified", maybeRecipe.get().getId()));
        }

        //SETTER, MAPPER OVERKILL?
        maybeRecipe.get().setInstructions(updateRecipeInstructionsCommand.instructions());
        Recipe updatedRecipe = recipeRepository.save(maybeRecipe.get());

        return updatedRecipe;
    }

    @Transactional
    public void removeInstructionsFromRecipe(Long recipeId) {
        Optional<Recipe> maybeRecipe = recipeRepository.findById(recipeId);
        if (maybeRecipe.isEmpty())
            throw new RecipeNotFoundException(String.format("No recipe with id %d found", recipeId));

        if (maybeRecipe.get().isFinalized()) {
            throw new RecipeFinalizedException(String.format("Recipe with id %d is finalized and cannot be modified", recipeId));
        }

        maybeRecipe.get().setInstructions(null);
        recipeRepository.save(maybeRecipe.get());
    }


}
