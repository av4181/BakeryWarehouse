package org.example.bakery2.services.mockito;

import org.example.bakery2.entities.Recipe;
import org.example.bakery2.exceptions.AddInstructionsToRecipeCommandNotFoundException;
import org.example.bakery2.exceptions.IncompleteException;
import org.example.bakery2.exceptions.RecipeFinalizedException;
import org.example.bakery2.exceptions.RecipeNotFoundException;
import org.example.bakery2.exceptions.UpdateRecipeInstructionsCommandNotFoundException;
import org.example.bakery2.repositories.RecipeRepository;
import org.example.bakery2.services.RecipeInstructionService;
import org.example.bakery2.services.RecipeService;
import org.example.bakery2.services.mockito.fixtures.RecipeFixtures;
import org.example.bakery2.services.mockito.fixtures.RecipeInstructionFixtures;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class RecipeInstructionServiceTest {

    @InjectMocks
    private RecipeInstructionService recipeInstructionService;

    @Mock
    private RecipeService recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @Test
    void addInstructionsToRecipe() {
        Recipe newRecipe = RecipeFixtures.getNewRecipeWithoutInstructions();
        Mockito.when(recipeRepository.findById(1L)).thenReturn(Optional.of(newRecipe));

        recipeInstructionService.addInstructionsToRecipe(1L, RecipeInstructionFixtures.getAddInstructionsToRecipeCommand());

        assertEquals(1L, newRecipe.getId());
        assertEquals("1.2.3", newRecipe.getInstructions());
        assertEquals("New Recipe", newRecipe.getName());
        assertEquals("New Product", newRecipe.getProduct().getName());
    }

    @Test
    void addInstructionsToRecipeNullCommand() {
        assertThrows(AddInstructionsToRecipeCommandNotFoundException.class, () -> recipeInstructionService.addInstructionsToRecipe(1L, null));
    }

    @Test
    void addInstructionsToRecipeNotFound() {
        assertThrows(RecipeNotFoundException.class, () -> recipeInstructionService.addInstructionsToRecipe(99L, RecipeInstructionFixtures.getAddInstructionsToRecipeCommand()));
    }

    @Test
    void addInstructionsToRecipeEmpty() {
        Mockito.when(recipeRepository.findById(1L)).thenReturn(Optional.of(RecipeFixtures.getNewRecipeFinalized()));
        assertThrows(IncompleteException.class, () -> recipeInstructionService.addInstructionsToRecipe(1L, RecipeInstructionFixtures.getAddInstructionsToRecipeCommandEmpty()));
    }

    @Test
    void addIngredientToRecipeIsFinalized() {
        Mockito.when(recipeRepository.findById(1L)).thenReturn(Optional.of(RecipeFixtures.getNewRecipeFinalized()));
        assertThrows(RecipeFinalizedException.class, () -> recipeInstructionService.addInstructionsToRecipe(1L, RecipeInstructionFixtures.getAddInstructionsToRecipeCommand()));
    }

    @Test
    void updateRecipeInstructions() {
        Recipe recipeToBeUpdated = RecipeFixtures.getNewRecipe();
        Mockito.when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipeToBeUpdated));

        recipeToBeUpdated.setInstructions("1.2.3.4");
        Mockito.when(recipeRepository.save(any(Recipe.class))).thenReturn(recipeToBeUpdated);

        Recipe recipe = recipeInstructionService.updateRecipeInstructions(1L, RecipeInstructionFixtures.getUpdateRecipeInstructionsCommand());

        assertEquals("1.2.3.4", recipe.getInstructions());
        assertEquals("New Recipe", recipe.getName());
        assertEquals("New Product", recipe.getProduct().getName());
    }

    @Test
    void updateRecipeInstructionsNullCommand() {
        assertThrows(UpdateRecipeInstructionsCommandNotFoundException.class, () -> recipeInstructionService.updateRecipeInstructions(1L, null));
    }

    @Test
    void updateRecipeInstructionsRecipeNotFound() {
        assertThrows(RecipeNotFoundException.class, () -> recipeInstructionService.updateRecipeInstructions(1L, RecipeInstructionFixtures.getUpdateRecipeInstructionsCommand()));
    }

    @Test
    void updateRecipeInstructionsEmpty() {
        Mockito.when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(RecipeFixtures.getNewRecipe()));
        assertThrows(IncompleteException.class, () -> recipeInstructionService.updateRecipeInstructions(1L, RecipeInstructionFixtures.getUpdateRecipeInstructionsCommandEmpty()));
    }

    @Test
    void updateRecipeInstructionsRecipeFinalized() {
        Mockito.when(recipeRepository.findById(1L)).thenReturn(Optional.of(RecipeFixtures.getNewRecipeFinalized()));
        assertThrows(RecipeFinalizedException.class, () -> recipeInstructionService.updateRecipeInstructions(1L, RecipeInstructionFixtures.getUpdateRecipeInstructionsCommand()));
    }


    @Test
    void removeInstructionsFromRecipe() {
        Recipe newRecipe = RecipeFixtures.getNewRecipe();
        Mockito.when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(newRecipe));
        recipeInstructionService.removeInstructionsFromRecipe(1L);

        assertNull(newRecipe.getInstructions());
    }

    @Test
    void removeInstructionsFromRecipeNotFound() {
        assertThrows(RecipeNotFoundException.class, () -> recipeInstructionService.removeInstructionsFromRecipe(1L));
    }

    @Test
    void removeInstructionsFromRecipeFinalized() {
        Mockito.when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(RecipeFixtures.getNewRecipeFinalized()));
        assertThrows(RecipeFinalizedException.class, () -> recipeInstructionService.removeInstructionsFromRecipe(1L));
    }


}