package org.example.bakery2.mappers;


import org.example.bakery2.entities.Recipe;
import org.example.bakery2.entities.commands.recipe.CreateRecipeCommand;
import org.example.bakery2.entities.commands.recipe.UpdateRecipeCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecipeMapper {

    Recipe convertToEntity(CreateRecipeCommand createRecipeCommand);

    Recipe convertToEntity(UpdateRecipeCommand updateRecipeCommand);

    default Recipe modifyEntity(Recipe recipeToBeUpdated, UpdateRecipeCommand updateRecipeCommand) {
        recipeToBeUpdated.setName(updateRecipeCommand.name());

        return recipeToBeUpdated;
    }

}
