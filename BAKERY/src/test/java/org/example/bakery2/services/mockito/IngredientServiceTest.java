package org.example.bakery2.services.mockito;

import org.example.bakery2.entities.Ingredient;
import org.example.bakery2.exceptions.DuplicateIngredientException;
import org.example.bakery2.exceptions.IncompleteException;
import org.example.bakery2.exceptions.IngredientCommandNotFoundException;
import org.example.bakery2.mappers.IngredientMapper;
import org.example.bakery2.mappers.IngredientMapperImpl;
import org.example.bakery2.repositories.IngredientRepository;
import org.example.bakery2.services.IngredientService;
import org.example.bakery2.services.mockito.fixtures.IngredientFixtures;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @InjectMocks
    private IngredientService ingredientService;

    @Mock
    private IngredientRepository ingredientRepository;

    @Spy
    private IngredientMapper ingredientMapper = new IngredientMapperImpl();

    @Test
    void findAllIngredients() {
    }

    @Test
    void createIngredient() {
        Ingredient ingredientEntity = ingredientMapper.convertToEntity(IngredientFixtures.getCreateIngredientCommand());
        Mockito.when(ingredientRepository.save(any(Ingredient.class))).thenReturn(ingredientEntity);

        Ingredient ingredient = ingredientService.createIngredient(IngredientFixtures.getCreateIngredientCommand());

        assertEquals("New ingredient", ingredient.getName());
    }

    @Test
    void createIngredientNull() {
        assertThrows(IngredientCommandNotFoundException.class, () -> ingredientService.createIngredient(null));
    }

    @Test
    void createIngredientNoName() {
        assertThrows(IncompleteException.class, () -> ingredientService.createIngredient(IngredientFixtures.getCreateIngredientCommandNoName()));
    }

    @Test
    void createIngredientDuplicateName() {
        Mockito.when(ingredientRepository.existsByNameIgnoringCase(IngredientFixtures.getCreateIngredientCommand().name())).thenReturn(true);
        assertThrows(DuplicateIngredientException.class, () -> ingredientService.createIngredient(IngredientFixtures.getCreateIngredientCommand()));
    }

    //TODO add when used
    @Test
    void deleteIngredientById() {

    }
}