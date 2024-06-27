package org.example.bakery2.repositories;

import org.example.bakery2.entities.Ingredient;
import org.example.bakery2.entities.Recipe;
import org.example.bakery2.entities.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {

    Optional<List<RecipeIngredient>> findAllByRecipeId(Long recipeId);

    boolean existsByRecipeAndIngredient(Recipe recipe, Ingredient ingredient);

}
