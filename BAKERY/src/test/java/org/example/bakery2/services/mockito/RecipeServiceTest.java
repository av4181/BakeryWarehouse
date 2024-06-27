package org.example.bakery2.services.mockito;

import org.example.bakery2.entities.Recipe;
import org.example.bakery2.entities.RecipeIngredient;
import org.example.bakery2.exceptions.DuplicateRecipeException;
import org.example.bakery2.exceptions.IncompleteException;
import org.example.bakery2.exceptions.IngredientNotFoundException;
import org.example.bakery2.exceptions.ProductNotFoundException;
import org.example.bakery2.exceptions.RecipeCommandNotFoundException;
import org.example.bakery2.exceptions.RecipeFinalizedException;
import org.example.bakery2.exceptions.RecipeIngredientNotFoundException;
import org.example.bakery2.exceptions.RecipeNotFoundException;
import org.example.bakery2.mappers.ProductMapper;
import org.example.bakery2.mappers.ProductMapperImpl;
import org.example.bakery2.mappers.RecipeIngredientMapper;
import org.example.bakery2.mappers.RecipeIngredientMapperImpl;
import org.example.bakery2.mappers.RecipeMapper;
import org.example.bakery2.mappers.RecipeMapperImpl;
import org.example.bakery2.repositories.RecipeIngredientRepository;
import org.example.bakery2.repositories.RecipeRepository;
import org.example.bakery2.services.IngredientService;
import org.example.bakery2.services.RecipeService;
import org.example.bakery2.services.mockito.fixtures.IngredientFixtures;
import org.example.bakery2.services.mockito.fixtures.RecipeFixtures;
import org.example.bakery2.services.mockito.fixtures.RecipeIngredientFixtures;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @InjectMocks
    private RecipeService recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeIngredientRepository recipeIngredientRepository;

    @Mock
    private IngredientService ingredientService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Spy
    private RecipeMapper recipeMapper = new RecipeMapperImpl();

    @Spy
    private ProductMapper productMapper = new ProductMapperImpl();

    @Spy
    private RecipeIngredientMapper recipeIngredientMapper = new RecipeIngredientMapperImpl();

    @Test
    void findAllRecipes() {
    }

    @Test
    void findRecipeForProduct() {
        Mockito.when(recipeRepository.findByProductId(anyLong())).thenReturn(Optional.of(RecipeFixtures.getNewRecipe()));
        Optional<Recipe> recipe = recipeService.findRecipeForProduct(1L);

        assertEquals("New Recipe", recipe.get().getName());
        assertEquals("New Product", recipe.get().getProduct().getName());
    }

    @Test
    void getRecipeById() {
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(RecipeFixtures.getNewRecipe()));
        Recipe recipe = recipeService.getRecipeById(1L);

        assertNotNull(recipe);
        assertEquals("New Recipe", recipe.getName());
        assertEquals(1L, recipe.getId());
        assertEquals("New Product", recipe.getProduct().getName());
        assertEquals("1.2.3", recipe.getInstructions());
        assertFalse(recipe.isFinalized());
    }

    @Test
    void findAllRecipesForProduct() {
    }

    @Test
    void createRecipe() {
        Recipe recipeEntity = recipeMapper.convertToEntity(RecipeFixtures.getCreateRecipeCommand());
        Mockito.when(recipeRepository.save(any(Recipe.class))).thenReturn(recipeEntity);

        Recipe recipe = recipeService.createRecipe(RecipeFixtures.getCreateRecipeCommand());

        assertEquals("New recipe", recipe.getName());
        assertEquals("New Product", recipe.getProduct().getName());
        assertNull(recipe.getInstructions());
        assertFalse(recipe.isFinalized());
    }

    @Test
    void createRecipeNull() {
        assertThrows(RecipeCommandNotFoundException.class, () -> recipeService.createRecipe(null));
    }

    @Test
    void createRecipeNoName() {
        assertThrows(IncompleteException.class, () -> recipeService.createRecipe(RecipeFixtures.getCreateRecipeCommandNoName()));
    }

    @Test
    void createRecipeNoProduct() {
        assertThrows(ProductNotFoundException.class, () -> recipeService.createRecipe(RecipeFixtures.getCreateRecipeCommandNoProduct()));
    }

    @Test
    void createRecipeWithDuplicateName() {
        Mockito.when(recipeRepository.existsByNameIgnoringCase(RecipeFixtures.getCreateRecipeCommand().name())).thenReturn(true);
        assertThrows(DuplicateRecipeException.class, () -> recipeService.createRecipe(RecipeFixtures.getCreateRecipeCommand()));
    }

    @Test
    void addIngredientToRecipe() {
        Mockito.when(ingredientService.findAllIngredients()).thenReturn(IngredientFixtures.getNewIngredientList());
        Mockito.when(recipeRepository.findById(1L)).thenReturn(Optional.of(RecipeFixtures.getNewRecipe()));
        Mockito.when(recipeIngredientRepository.findAllByRecipeId(1L)).thenReturn(RecipeIngredientFixtures.getNewRecipeIngredientList());

        recipeService.addIngredientToRecipe(1L, RecipeFixtures.getAddIngredientsToRecipeCommand());

        List<RecipeIngredient> recipeIngredientList = recipeService.getRecipeIngredientsForRecipe(1L);
        RecipeIngredient recipeIngredient = recipeIngredientList.get(0);

        assertEquals(1L, recipeIngredient.getId());
        assertEquals("Ingredient", recipeIngredient.getIngredient().getName());
        assertEquals("New Recipe", recipeIngredient.getRecipe().getName());
    }

    @Test
    void addIngredientToRecipeNullCommand() {
        assertThrows(RecipeCommandNotFoundException.class, () -> recipeService.addIngredientToRecipe(1L, null));
    }

    @Test
    void addIngredientToRecipeNotFound() {
        assertThrows(RecipeNotFoundException.class, () -> recipeService.addIngredientToRecipe(99L, RecipeFixtures.getAddIngredientsToRecipeCommand()));
    }

    @Test
    void addIngredientToRecipeNull() {
        assertThrows(RecipeNotFoundException.class, () -> recipeService.addIngredientToRecipe(null, RecipeFixtures.getAddIngredientsToRecipeCommand()));
    }

    @Test
    void addIngredientToRecipeIsFinalized() {
        Mockito.when(recipeRepository.findById(1L)).thenReturn(Optional.of(RecipeFixtures.getNewRecipeFinalized()));
        assertThrows(RecipeFinalizedException.class, () -> recipeService.addIngredientToRecipe(1L, RecipeFixtures.getAddIngredientsToRecipeCommand()));
    }

    @Test
    void addIngredientToRecipeIngredientNotFound() {
        Mockito.when(recipeRepository.findById(1L)).thenReturn(Optional.of(RecipeFixtures.getNewRecipe()));
        Mockito.when(ingredientService.findAllIngredients()).thenReturn(IngredientFixtures.getNewRandomIngredientList());
        assertThrows(IngredientNotFoundException.class, () -> recipeService.addIngredientToRecipe(1L, RecipeFixtures.getAddIngredientsToRecipeCommand()));
    }

    //TODO FIX STUBBING PROBLEM
//    @Test
//    void addIngredientToRecipeIngredientDuplicate() {
//        Mockito.when(ingredientService.findAllIngredients()).thenReturn(IngredientFixtures.getNewIngredientList());
//        Recipe newRecipe = RecipeFixtures.getNewRecipe();
//        Mockito.when(recipeRepository.findById(1L)).thenReturn(Optional.of(newRecipe));
////        when(recipeIngredientRepository.findAllByRecipeId(1L)).thenReturn(RecipeIngredientFixtures.getNewRecipeIngredientList());
//
////        Ingredient newIngredient = IngredientFixtures.getNewIngredient();
//        Mockito.when(recipeIngredientRepository.existsByRecipeAndIngredient(newRecipe, IngredientFixtures.getNewIngredientList().get(0))).thenReturn(true);
//
//            recipeService.addIngredientToRecipe(1L, RecipeFixtures.getAddIngredientsToRecipeCommand());
////        assertThrows(DuplicateIngredientException.class, () -> recipeService.addIngredientToRecipe(1L, RecipeFixtures.getAddIngredientsToRecipeCommand()));
//
//    }

    @Test
    void updateRecipe() {
        Recipe recipeToBeUpdated = RecipeFixtures.getNewRecipe();
        Mockito.when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipeToBeUpdated));

        Recipe recipeEntity = recipeMapper.modifyEntity(recipeToBeUpdated, RecipeFixtures.getUpdateRecipeCommand());
        Mockito.when(recipeRepository.save(any(Recipe.class))).thenReturn(recipeEntity);

        Recipe recipe = recipeService.updateRecipe(1L, RecipeFixtures.getUpdateRecipeCommand());

        assertEquals("New Recipe name", recipe.getName());
        assertEquals("New Product", recipe.getProduct().getName());
        assertEquals("1.2.3", recipe.getInstructions());
        assertFalse(recipe.isFinalized());
    }

    @Test
    void updateRecipeNullCommand() {
        assertThrows(RecipeCommandNotFoundException.class, () -> recipeService.updateRecipe(1L, null));
    }

    @Test
    void updateRecipeNotFound() {
        assertThrows(RecipeNotFoundException.class, () -> recipeService.updateRecipe(null, RecipeFixtures.getUpdateRecipeCommand()));
    }

    @Test
    void updateRecipeFinalized() {
        Mockito.when(recipeRepository.findById(1L)).thenReturn(Optional.of(RecipeFixtures.getNewRecipeFinalized()));
        assertThrows(RecipeFinalizedException.class, () -> recipeService.updateRecipe(1L, RecipeFixtures.getUpdateRecipeCommand()));
    }

    @Test
    void updateRecipeNoName() {
        Mockito.when(recipeRepository.findById(1L)).thenReturn(Optional.of(RecipeFixtures.getNewRecipe()));
        assertThrows(IncompleteException.class, () -> recipeService.updateRecipe(1L, RecipeFixtures.getUpdateRecipeCommandNoName()));
    }

    @Test
    void updateRecipeDuplicateName() {
        Mockito.when(recipeRepository.findById(1L)).thenReturn(Optional.of(RecipeFixtures.getNewRecipe()));
        Mockito.when(recipeRepository.existsByNameIgnoringCase(RecipeFixtures.getNewRecipe().getName())).thenReturn(true);
        assertThrows(DuplicateRecipeException.class, () -> recipeService.updateRecipe(1L, RecipeFixtures.getUpdateRecipeCommandDuplicateName()));
    }

    @Test
    void getRecipeIngredientsForRecipe() {
        Mockito.when(recipeIngredientRepository.findAllByRecipeId(anyLong())).thenReturn((RecipeIngredientFixtures.getNewRecipeIngredientList()));
        Mockito.when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(RecipeFixtures.getRecipeWithIngredient()));

        List<RecipeIngredient> recipeIngredientList = recipeService.getRecipeIngredientsForRecipe(1L);

        assertNotNull(recipeIngredientList);
        assertEquals(1, recipeIngredientList.size());
        assertEquals("New Recipe", recipeIngredientList.get(0).getRecipe().getName());
        assertEquals("Ingredient", recipeIngredientList.get(0).getIngredient().getName());
    }

    @Test
    void getRecipeIngredientsForRecipeNotFound() {
        assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipeIngredientsForRecipe(99L));
    }

    @Test
    void getRecipeIngredientsForRecipeIngredientNotFound() {
        Mockito.when(recipeRepository.findById(1L)).thenReturn(Optional.of(RecipeFixtures.getNewRecipe()));
        assertThrows(RecipeIngredientNotFoundException.class, () -> recipeService.getRecipeIngredientsForRecipe(1L));
    }

    //TODO add when used

    @Test
    void deleteRecipeById() {
    }

    @Test
    void finalizeRecipe() {
        Mockito.when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(RecipeFixtures.getNewRecipe()));
        Mockito.when(recipeRepository.findByProductId(anyLong())).thenReturn(Optional.of(RecipeFixtures.getNewRecipe()));
        Mockito.when(recipeIngredientRepository.findAllByRecipeId(anyLong())).thenReturn(RecipeIngredientFixtures.getNewRecipeIngredientList());
        Mockito.when(recipeRepository.save(any(Recipe.class))).thenReturn(RecipeFixtures.getNewRecipeFinalized());

        Recipe recipe = recipeService.finalizeRecipe(1L);

        assertTrue(recipe.isFinalized());
    }

}