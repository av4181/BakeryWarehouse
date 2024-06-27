package org.example.bakery2.services.mockito.fixtures;

import org.example.bakery2.entities.Recipe;
import org.example.bakery2.entities.commands.recipe.AddIngredientsToRecipeCommand;
import org.example.bakery2.entities.commands.recipe.CreateRecipeCommand;
import org.example.bakery2.entities.commands.recipe.UpdateRecipeCommand;

public class RecipeFixtures {

    public static CreateRecipeCommand getCreateRecipeCommand() {
        return new CreateRecipeCommand("New recipe", ProductFixtures.getNewProduct());
    }

    public static Recipe getNewRecipe() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("New Recipe");
        recipe.setProduct(ProductFixtures.getNewProduct());
        recipe.setFinalized(false);
        recipe.setInstructions("1.2.3");
        return recipe;
    }

    public static Recipe getNewRecipeWithoutInstructions() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("New Recipe");
        recipe.setProduct(ProductFixtures.getNewProduct());
        recipe.setFinalized(false);
        return recipe;
    }


    public static Recipe getNewRecipeFinalized() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("New Recipe");
        recipe.setProduct(ProductFixtures.getNewProduct());
        recipe.setFinalized(true);
        recipe.setInstructions("1.2.3");
        return recipe;
    }

    public static Recipe getRecipeWithIngredient() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("New Recipe");
        recipe.setProduct(ProductFixtures.getNewProduct());
        recipe.setFinalized(false);
        recipe.setInstructions("1.2.3");
        return recipe;
    }

    public static CreateRecipeCommand getCreateRecipeCommandNoName() {
        return new CreateRecipeCommand("", ProductFixtures.getNewProduct());
    }

    public static CreateRecipeCommand getCreateRecipeCommandNoProduct() {
        return new CreateRecipeCommand("New recipe", null);
    }

    public static AddIngredientsToRecipeCommand getAddIngredientsToRecipeCommand() {
        return new AddIngredientsToRecipeCommand("Ingredient", "1", "KG");
    }

    public static UpdateRecipeCommand getUpdateRecipeCommand() {
        return new UpdateRecipeCommand("New Recipe name");
    }

    public static UpdateRecipeCommand getUpdateRecipeCommandNoName() {
        return new UpdateRecipeCommand("");
    }

    public static UpdateRecipeCommand getUpdateRecipeCommandDuplicateName() {
        return new UpdateRecipeCommand("New Recipe");
    }

}
