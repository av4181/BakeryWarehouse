package org.example.bakery2.mappers;

import org.example.bakery2.entities.RecipeIngredient;
import org.example.bakery2.entities.commands.recipe.AddIngredientsToRecipeCommand;
import org.example.bakery2.entities.commands.recipeIngredient.UpdateRecipeIngredientCommand;
import org.example.bakery2.entities.dto.RecipeIngredientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RecipeIngredientMapper {

    @Mappings({
            @Mapping(target = "ingredient", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "recipe", ignore = true)
    })
    RecipeIngredient convertToEntity(AddIngredientsToRecipeCommand addIngredientsToRecipeCommand);

    default RecipeIngredient modifyEntity(RecipeIngredient recipeIngredientToBeUpdated, UpdateRecipeIngredientCommand updateRecipeIngredientCommand) {
        recipeIngredientToBeUpdated.setAmount(updateRecipeIngredientCommand.amount());
        recipeIngredientToBeUpdated.setUnitOfMeasurement(updateRecipeIngredientCommand.unitOfMeasurement());

        return recipeIngredientToBeUpdated;
    }

    default List<RecipeIngredientDTO> convertToDTO(List<RecipeIngredient> recipeIngredientList) {
        List<RecipeIngredientDTO> recipeIngredientDTOList = new ArrayList<RecipeIngredientDTO>();
        for (RecipeIngredient recipeIngredient : recipeIngredientList) {
            RecipeIngredientDTO recipeIngredientDTO = new RecipeIngredientDTO();
            recipeIngredientDTO.setIngredient(recipeIngredient.getIngredient());
            recipeIngredientDTO.setAmount(recipeIngredient.getAmount());
            recipeIngredientDTO.setUnitOfMeasurement(recipeIngredient.getUnitOfMeasurement());

            recipeIngredientDTOList.add(recipeIngredientDTO);
        }
        return recipeIngredientDTOList;
    }


}
