package org.example.warehouse2.services;

import org.example.warehouse2.exceptions.IngredientNotFoundException;
import org.example.warehouse2.exceptions.SupplierNotFoundException;

public interface StockService {

    String updateStock(String itemNumber, int stockCount);
    void placeReorderForIngredient(String ingredientNumber) throws IngredientNotFoundException, SupplierNotFoundException;
}

