package org.example.warehouse2.serviceTests.mockito;

import org.example.warehouse2.controllers.api.dto.IngredientDTO;
import org.example.warehouse2.exceptions.IngredientNotFoundException;
import org.example.warehouse2.model.Ingredient;
import org.example.warehouse2.model.IngredientStatus;
import org.example.warehouse2.model.Supplier;
import org.example.warehouse2.persistence.mappers.IngredientMapper;
import org.example.warehouse2.persistence.mappers.IngredientMapperImpl;
import org.example.warehouse2.persistence.repositories.IngredientRepository;
import org.example.warehouse2.persistence.repositories.SupplierRepository;
import org.example.warehouse2.services.IngredientServiceImpl;
import org.example.warehouse2.serviceTests.mockito.fixtures.IngredientFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class IngredientServiceImplTest {
    @Mock
    private IngredientRepository ingredientRepository;
    @Mock
    private SupplierRepository supplierRepository;
    @Mock
    private IngredientMapper ingredientMapper;
    @InjectMocks
    private IngredientServiceImpl ingredientService;

    @Test
    void testGetIngredients() {
        Ingredient mockIngredient1 = IngredientFixtures.createIngredient();
        Ingredient mockIngredient2 = IngredientFixtures.createIngredient();
        mockIngredient2.setId(2L);
        mockIngredient2.setIngredientNumber("ING-002");
        mockIngredient2.setStatus(IngredientStatus.OUT_OF_STOCK);
        mockIngredient2.setStock(50);

        List<Ingredient> mockIngredients = List.of(mockIngredient1, mockIngredient2);

        Mockito.when(ingredientRepository.findAll()).thenReturn(mockIngredients);

        Mockito.when(ingredientMapper.toDTO(any(Ingredient.class))).thenAnswer(i -> {
            Ingredient ingredient = i.getArgument(0);
            return new IngredientDTO(ingredient.getId(), ingredient.getIngredientNumber(), ingredient.getName(), ingredient.getStock(), ingredient.getStatus(), null, ingredient.getExpirationDate());
        });

        List<IngredientDTO> result = ingredientService.getIngredients();

        assertEquals(2, result.size());
        assertEquals("Flour", result.get(0).getName());
        assertEquals(100, result.get(0).getStock());
        assertEquals(IngredientStatus.AVAILABLE, result.get(0).getStatus());
        assertEquals("Sugar", result.get(1).getName());
        assertEquals(50, result.get(1).getStock());
        assertEquals(IngredientStatus.OUT_OF_STOCK, result.get(1).getStatus());
    }

    @Test
    void testGetIngredientByNumber_Found() throws IngredientNotFoundException {
        Ingredient mockIngredient = IngredientFixtures.createIngredient();

        Mockito.when(ingredientRepository.findByIngredientNumber("ING-001")).thenReturn(Optional.of(mockIngredient));

        Ingredient result = ingredientService.getIngredientByNumber("ING-001");

        assertEquals(mockIngredient, result);
    }

    @Test
    void testGetIngredientByNumber_NotFound() {
        Mockito.when(ingredientRepository.findByIngredientNumber("Nonexistent Ingredient")).thenReturn(Optional.empty());

        assertThrows(IngredientNotFoundException.class, () -> {
            ingredientService.getIngredientByNumber("Nonexistent Ingredient");
        });
    }

    //US-57 opvragen van ingrediënten die bijna vervallen
    @Test
    void testGetIngredientsExpiringBefore() {
        Supplier mockSupplier = new Supplier(null, "Test Supplier", "John Doe", "john.doe@example.com", "1234567890", new ArrayList<>());
        Ingredient mockIngredient1 = IngredientFixtures.createIngredient();
        Ingredient mockIngredient2 = IngredientFixtures.createIngredient();
        mockIngredient2.setId(2L);
        mockIngredient2.setIngredientNumber("ING-002");
        mockIngredient2.setExpirationDate(LocalDate.now().plusDays(3));

        LocalDate expirationDate = LocalDate.now().plusDays(5);
        Mockito.when(ingredientRepository.findByExpirationDateBefore(expirationDate)).thenReturn(List.of(mockIngredient1, mockIngredient2));

        List<IngredientDTO> result = ingredientService.getIngredientsExpiringBefore(expirationDate);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(dto -> dto.getExpirationDate().isBefore(expirationDate)));
    }
}