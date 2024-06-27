package org.example.bakery2.services.mockito.fixtures;

import org.example.bakery2.entities.RecipeIngredient;
import org.example.bakery2.entities.commands.recipeIngredient.UpdateRecipeIngredientCommand;
import org.example.bakery2.enums.UnitOfMeasurementEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecipeIngredientFixtures {
    public static Optional<List<RecipeIngredient>> getNewRecipeIngredientList() {
        List<RecipeIngredient> recipeIngredientList = new ArrayList<>();
        recipeIngredientList.add(getNewRecipeIngredientWithRecipe());
        return Optional.of(recipeIngredientList);
    }

    public static RecipeIngredient getNewRecipeIngredientWithRecipe() {
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setId(1L);
        recipeIngredient.setRecipe(RecipeFixtures.getNewRecipe());
        recipeIngredient.setIngredient(IngredientFixtures.getNewIngredient());
        recipeIngredient.setAmount(1);
        recipeIngredient.setUnitOfMeasurement(UnitOfMeasurementEnum.KG);
        return recipeIngredient;
    }

    public static RecipeIngredient getNewRecipeIngredientWithoutRecipe() {
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setId(1L);
        recipeIngredient.setIngredient(IngredientFixtures.getNewIngredient());
        recipeIngredient.setAmount(1);
        recipeIngredient.setUnitOfMeasurement(UnitOfMeasurementEnum.KG);
        return recipeIngredient;
    }

    public static RecipeIngredient getNewRecipeIngredientWithFinalizedRecipe() {
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setId(1L);
        recipeIngredient.setRecipe(RecipeFixtures.getNewRecipeFinalized());
        recipeIngredient.setIngredient(IngredientFixtures.getNewIngredient());
        recipeIngredient.setAmount(1);
        recipeIngredient.setUnitOfMeasurement(UnitOfMeasurementEnum.KG);
        return recipeIngredient;
    }

    public static UpdateRecipeIngredientCommand getUpdateRecipeIngredientCommand() {
        return new UpdateRecipeIngredientCommand(100, UnitOfMeasurementEnum.L);
    }

//    public static UpdateRecipeIngredientCommand getUpdateRecipeIngredientCommandNoAmount() {
//        return new UpdateRecipeIngredientCommand(null, UnitOfMeasurementEnum.L);
//    }
}
