package org.example.bakery2.entities.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.bakery2.entities.Ingredient;
import org.example.bakery2.enums.UnitOfMeasurementEnum;

@Getter
@Setter
public class RecipeIngredientDTO {

    private Ingredient ingredient;
    private double amount;
    private UnitOfMeasurementEnum unitOfMeasurement;

}
