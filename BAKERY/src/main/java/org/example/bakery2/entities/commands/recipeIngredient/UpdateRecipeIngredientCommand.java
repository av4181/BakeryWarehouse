package org.example.bakery2.entities.commands.recipeIngredient;

import org.example.bakery2.enums.UnitOfMeasurementEnum;

public record UpdateRecipeIngredientCommand(double amount, UnitOfMeasurementEnum unitOfMeasurement) {
}
