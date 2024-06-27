package org.example.bakery2.entities.commands.recipe;

public record AddIngredientsToRecipeCommand(String ingredient, String amount, String unitOfMeasurement) {
}

