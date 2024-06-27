package org.example.bakery2.repositories;

import org.example.bakery2.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Optional<Recipe> findByProductId(Long productId);

    Optional<List<Recipe>> findAllByProductId(Long productId);

    boolean existsByNameIgnoringCase(String recipeName);

}
