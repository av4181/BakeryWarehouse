package org.example.bakery2.services.mockito.fixtures;

import org.example.bakery2.entities.Ingredient;
import org.example.bakery2.entities.commands.ingredient.CreateIngredientCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IngredientFixtures {

    public static CreateIngredientCommand getCreateIngredientCommand() {
        return new CreateIngredientCommand("New ingredient");
    }

    public static CreateIngredientCommand getCreateIngredientCommandNoName() {
        return new CreateIngredientCommand("");
    }

    public static List<Ingredient> getNewIngredientList() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(getNewIngredient());
        return ingredients;
    }

    public static List<Ingredient> getNewRandomIngredientList() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(getNewRandomIngredient());
        return ingredients;
    }

    public static Ingredient getNewIngredient() {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("Ingredient");
        ingredient.setUuid(UUID.randomUUID());

        return ingredient;
    }

    public static Ingredient getNewRandomIngredient() {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("Random Ingredient");
        ingredient.setUuid(UUID.randomUUID());

        return ingredient;
    }
}
