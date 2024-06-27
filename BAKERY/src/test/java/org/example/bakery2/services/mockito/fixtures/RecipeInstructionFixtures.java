package org.example.bakery2.services.mockito.fixtures;

import org.example.bakery2.entities.commands.recipe.AddInstructionsToRecipeCommand;
import org.example.bakery2.entities.commands.recipe.UpdateRecipeInstructionsCommand;

public class RecipeInstructionFixtures {
    public static AddInstructionsToRecipeCommand getAddInstructionsToRecipeCommand() {
        return new AddInstructionsToRecipeCommand("1.2.3");
    }

    public static AddInstructionsToRecipeCommand getAddInstructionsToRecipeCommandEmpty() {
        return new AddInstructionsToRecipeCommand("");
    }

    public static UpdateRecipeInstructionsCommand getUpdateRecipeInstructionsCommand() {
        return new UpdateRecipeInstructionsCommand("1.2.3.4");
    }

    public static UpdateRecipeInstructionsCommand getUpdateRecipeInstructionsCommandEmpty() {
        return new UpdateRecipeInstructionsCommand("");
    }
}
