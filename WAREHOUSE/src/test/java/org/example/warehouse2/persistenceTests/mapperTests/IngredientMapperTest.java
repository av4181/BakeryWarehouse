package org.example.warehouse2.persistenceTests.mapperTests;

import org.example.warehouse2.controllers.api.dto.IngredientDTO;
import org.example.warehouse2.model.Ingredient;
import org.example.warehouse2.model.IngredientStatus;
import org.example.warehouse2.model.Supplier;
import org.example.warehouse2.persistence.mappers.IngredientMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IngredientMapperTest {

    private final IngredientMapper ingredientMapper = Mappers.getMapper(IngredientMapper.class);

    @Test
    void testToDTO() {
        Supplier supplier = new Supplier(1L, "Test Supplier", "John Doe", "john.doe@example.com", "1234567890", new ArrayList<>());
        Ingredient ingredient = new Ingredient(1L, "ING-001", "Flour", 100, IngredientStatus.AVAILABLE, supplier, LocalDate.now());

        IngredientDTO ingredientDTO = ingredientMapper.toDTO(ingredient);

        assertEquals(ingredient.getId(), ingredientDTO.getId());
        assertEquals(ingredient.getIngredientNumber(), ingredientDTO.getIngredientNumber());
        assertEquals(ingredient.getName(), ingredientDTO.getName());
        assertEquals(ingredient.getStock(), ingredientDTO.getStock());
        assertEquals(ingredient.getStatus(), ingredientDTO.getStatus());
        assertEquals(ingredient.getSupplier().getId(), ingredientDTO.getSupplierId());
        assertEquals(ingredient.getExpirationDate(), ingredientDTO.getExpirationDate());
    }
}
