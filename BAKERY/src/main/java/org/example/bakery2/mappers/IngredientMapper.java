package org.example.bakery2.mappers;

import org.example.bakery2.entities.Ingredient;
import org.example.bakery2.entities.commands.ingredient.CreateIngredientCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    Ingredient convertToEntity(CreateIngredientCommand createIngredientCommand);

}
