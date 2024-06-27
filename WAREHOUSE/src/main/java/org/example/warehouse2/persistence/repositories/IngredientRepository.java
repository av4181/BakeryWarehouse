package org.example.warehouse2.persistence.repositories;

import jakarta.transaction.Transactional;
import org.example.warehouse2.exceptions.IngredientNotFoundException;
import org.example.warehouse2.model.Ingredient;
import org.example.warehouse2.model.IngredientStatus;
import org.example.warehouse2.model.Supplier;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("JPA")
@Transactional
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    Optional<Ingredient> findByIngredientNumber(String ingredientNumber);
    List<Ingredient> findByStatus(IngredientStatus status);
    //US-57 opzoeken lijst van ingrediënten met bepaalde vervaldatum
    @Query("SELECT i FROM Ingredient i WHERE i.expirationDate < :expirationDate")
    List<Ingredient> findByExpirationDateBefore(@Param("expirationDate") LocalDate expirationDate);
    boolean existsByIngredientNumber(String ingredientNumber);
    List<Ingredient> findByStockLessThan(int threshold);
    List<Ingredient> findBySupplier(Supplier supplier);
    List<Ingredient> findBySupplierId(Long supplierId);
    @Query("SELECT i FROM Ingredient i JOIN FETCH i.supplier WHERE i.stock < :threshold")
    List<Ingredient> findLowStockIngredientsWithSupplier(@Param("threshold") int threshold);
}
