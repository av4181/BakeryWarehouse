package org.example.bakery2.services.mockito;

import org.example.bakery2.entities.RecipeIngredient;
import org.example.bakery2.enums.UnitOfMeasurementEnum;
import org.example.bakery2.exceptions.RecipeFinalizedException;
import org.example.bakery2.exceptions.RecipeIngredientCommandNotFoundException;
import org.example.bakery2.exceptions.RecipeIngredientNotFoundException;
import org.example.bakery2.exceptions.RecipeNotFoundException;
import org.example.bakery2.mappers.RecipeIngredientMapper;
import org.example.bakery2.mappers.RecipeIngredientMapperImpl;
import org.example.bakery2.repositories.RecipeIngredientRepository;
import org.example.bakery2.services.RecipeIngredientService;
import org.example.bakery2.services.mockito.fixtures.RecipeIngredientFixtures;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class RecipeIngredientServiceTest {

    @InjectMocks
    private RecipeIngredientService recipeIngredientService;

    @Mock
    private RecipeIngredientRepository recipeIngredientRepository;

    @Spy
    private RecipeIngredientMapper recipeIngredientMapper = new RecipeIngredientMapperImpl();

    @Test
    void removeRecipeIngredientByIdFromRecipe() {
        RecipeIngredient RecipeIngredientWithRecipeEntity = RecipeIngredientFixtures.getNewRecipeIngredientWithRecipe();
        Mockito.when(recipeIngredientRepository.findById(anyLong())).thenReturn(Optional.of(RecipeIngredientWithRecipeEntity));
        recipeIngredientService.removeRecipeIngredientByIdFromRecipe(1L);

        assertNull(RecipeIngredientWithRecipeEntity.getRecipe());
    }

    @Test
    void removeRecipeIngredientByIdFromRecipeRecipeIngredientNotFound() {
        assertThrows(RecipeIngredientNotFoundException.class, () -> recipeIngredientService.removeRecipeIngredientByIdFromRecipe(1L));
    }

    @Test
    void removeRecipeIngredientByIdFromRecipeRecipeNotFound() {
        Mockito.when(recipeIngredientRepository.findById(anyLong())).thenReturn(Optional.of(RecipeIngredientFixtures.getNewRecipeIngredientWithoutRecipe()));
        assertThrows(RecipeNotFoundException.class, () -> recipeIngredientService.removeRecipeIngredientByIdFromRecipe(1L));
    }

    @Test
    void removeRecipeIngredientByIdFromRecipeRecipeFinalized() {
        Mockito.when(recipeIngredientRepository.findById(anyLong())).thenReturn(Optional.of(RecipeIngredientFixtures.getNewRecipeIngredientWithFinalizedRecipe()));
        assertThrows(RecipeFinalizedException.class, () -> recipeIngredientService.removeRecipeIngredientByIdFromRecipe(1L));
    }

    @Test
    void updateRecipeIngredient() {
        RecipeIngredient recipeIngredientToBeUpdated = RecipeIngredientFixtures.getNewRecipeIngredientWithRecipe();
        Mockito.when(recipeIngredientRepository.findById(anyLong())).thenReturn(Optional.of(recipeIngredientToBeUpdated));

        RecipeIngredient recipeIngredientEntity = recipeIngredientMapper.modifyEntity(recipeIngredientToBeUpdated, RecipeIngredientFixtures.getUpdateRecipeIngredientCommand());
        Mockito.when(recipeIngredientRepository.save(any(RecipeIngredient.class))).thenReturn(recipeIngredientEntity);

        RecipeIngredient recipeIngredient = recipeIngredientService.updateRecipeIngredient(1L, RecipeIngredientFixtures.getUpdateRecipeIngredientCommand());

        assertEquals(100, recipeIngredient.getAmount());
        assertEquals(UnitOfMeasurementEnum.L, recipeIngredient.getUnitOfMeasurement());
        assertEquals("New Recipe", recipeIngredient.getRecipe().getName());
        assertEquals("Ingredient", recipeIngredient.getIngredient().getName());
    }

    @Test
    void updateRecipeIngredientNullCommand() {
        assertThrows(RecipeIngredientCommandNotFoundException.class, () -> recipeIngredientService.updateRecipeIngredient(1L, null));
    }

    @Test
    void updateRecipeIngredientNotFound() {
        assertThrows(RecipeIngredientNotFoundException.class, () -> recipeIngredientService.updateRecipeIngredient(1L, RecipeIngredientFixtures.getUpdateRecipeIngredientCommand()));
    }

    @Test
    void updateRecipeIngredientFinalized() {
        Mockito.when(recipeIngredientRepository.findById(1L)).thenReturn(Optional.of(RecipeIngredientFixtures.getNewRecipeIngredientWithFinalizedRecipe()));
        assertThrows(RecipeFinalizedException.class, () -> recipeIngredientService.updateRecipeIngredient(1L, RecipeIngredientFixtures.getUpdateRecipeIngredientCommand()));
    }

    //TODO fix when added null double
//    @Test
//    void updateRecipeIngredientNoAmount(){
//        Mockito.when(recipeIngredientRepository.findById(1L)).thenReturn(Optional.of(RecipeIngredientFixtures.getNewRecipeIngredientWithFinalizedRecipe()));
//        assertThrows(IncompleteException.class, () -> recipeIngredientService.updateRecipeIngredient(1L, RecipeIngredientFixtures.getUpdateRecipeIngredientCommandNoAmount()));
//    }


}