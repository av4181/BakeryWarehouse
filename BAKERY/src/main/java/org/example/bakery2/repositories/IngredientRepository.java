package org.example.bakery2.repositories;

import org.example.bakery2.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    boolean existsByNameIgnoringCase(String name);
}
