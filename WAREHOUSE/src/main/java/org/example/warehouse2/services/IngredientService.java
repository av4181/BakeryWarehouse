package org.example.warehouse2.services;

import org.example.warehouse2.controllers.api.dto.IngredientDTO;
import org.example.warehouse2.exceptions.IngredientNotFoundException;
import org.example.warehouse2.exceptions.SupplierNotFoundException;
import org.example.warehouse2.model.Ingredient;

import java.time.LocalDate;
import java.util.List;

public interface IngredientService {
    List<IngredientDTO> getIngredients();
    Ingredient getIngredientByNumber(String ingredientNumber) throws IngredientNotFoundException;
    Ingredient updateIngredient(String ingredientNumber, IngredientDTO ingredientDTO) throws IngredientNotFoundException;
    void deleteIngredient(String ingredientNumber) throws IngredientNotFoundException;
    void processNewIngredients(List<Ingredient> newIngredients);
    void setSupplierForIngredient(String ingredientNumber, Long supplierId) throws IngredientNotFoundException, SupplierNotFoundException;

    //US-57 warehouse manager moet een lijst kunnen trekken van ingrediënten die vervallen tegen bepaalde datum
    List<IngredientDTO> getIngredientsExpiringBefore(LocalDate expirationDate);
    List<IngredientDTO> getLowStockIngredients();
}
