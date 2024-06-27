package org.example.warehouse2.serviceTests.mockito.fixtures;

import org.example.warehouse2.model.Ingredient;
import org.example.warehouse2.model.IngredientStatus;
import org.example.warehouse2.model.Supplier;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IngredientFixtures {

    public static Ingredient createIngredient() {
        Supplier mockSupplier = new Supplier(1L, "Test Supplier", "John Doe", "john.doe@example.com", "1234567890", new ArrayList<>());
        return new Ingredient(1L, "ING-001", "Flour", 100, IngredientStatus.AVAILABLE, mockSupplier, LocalDate.now());
    }

    public static Ingredient createLowStockIngredient() {
        Supplier mockSupplier = new Supplier(1L, "Test Supplier", "John Doe", "john.doe@example.com", "1234567890", new ArrayList<>());
        return new Ingredient(2L, "ING-002", "Sugar", 5, IngredientStatus.AVAILABLE, mockSupplier, LocalDate.now().plusDays(10));
    }

}
