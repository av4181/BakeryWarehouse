package org.example.warehouse2.serviceTests.mockito.fixtures;

import org.example.warehouse2.model.Ingredient;
import org.example.warehouse2.model.IngredientStatus;
import org.example.warehouse2.model.Supplier;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StockFixtures {
    public static Ingredient createIngredientWithStock(String ingredientNumber, int stock) {
        Supplier mockSupplier = new Supplier(1L, "Test Supplier", "John Doe", "john.doe@example.com", "1234567890", new ArrayList<>());
        return new Ingredient(1L, ingredientNumber, "Flour", stock, IngredientStatus.AVAILABLE, mockSupplier, LocalDate.now());
    }
}
