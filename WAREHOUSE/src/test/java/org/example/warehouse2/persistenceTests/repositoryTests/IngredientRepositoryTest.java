package org.example.warehouse2.persistenceTests.repositoryTests;

import jakarta.transaction.Transactional;
import org.example.warehouse2.model.Ingredient;
import org.example.warehouse2.model.IngredientStatus;
import org.example.warehouse2.model.Supplier;
import org.example.warehouse2.persistence.repositories.IngredientRepository;
import org.example.warehouse2.persistence.repositories.SupplierRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Transactional
public class IngredientRepositoryTest {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    private Long testIngredientId;
    private Long testSupplierId;

    @BeforeEach
    @Transactional
    public void setup() {
        Supplier supplier = new Supplier();
        supplier.setName("Test Supplier");
        supplier.setContactPerson("John Doe");
        supplier.setEmail("john.doe@example.com");
        supplier.setPhoneNumber("1234567890");
        supplier.setSuppliedIngredients(new ArrayList<>());

        Supplier savedSupplier = supplierRepository.save(supplier);
        this.testSupplierId = savedSupplier.getId();

        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientNumber("ING-001");
        ingredient.setName("Flour");
        ingredient.setStock(100);
        ingredient.setStatus(IngredientStatus.AVAILABLE);
        ingredient.setExpirationDate(LocalDate.now().plusDays(1));
        ingredient.setSupplier(savedSupplier);

        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        this.testIngredientId = savedIngredient.getId();
    }

    @AfterEach
    public void teardown() {
        ingredientRepository.deleteAll();
        supplierRepository.deleteAll();
    }

    @Test
    void testFindByIngredientNumber() {
        Optional<Ingredient> foundIngredient = ingredientRepository.findByIngredientNumber("ING-001");

        assertTrue(foundIngredient.isPresent());
        assertEquals("Flour", foundIngredient.get().getName());
    }

    @Test
    public void testFindByStatus() {
        List<Ingredient> availableIngredients = ingredientRepository.findByStatus(IngredientStatus.AVAILABLE);
        assertFalse(availableIngredients.isEmpty());
    }

    @Test
    public void testFindByExpirationDateBefore() {
        LocalDate expirationDate = LocalDate.now().plusDays(1);
        List<Ingredient> expiringIngredients = ingredientRepository.findByExpirationDateBefore(expirationDate);
        assertFalse(expiringIngredients.isEmpty());
    }

    @Test
    public void testExistsByIngredientNumber() {
        assertTrue(ingredientRepository.existsByIngredientNumber("ING-001")); //BEstaat
        assertFalse(ingredientRepository.existsByIngredientNumber("ING-999")); // Bestaat niet
    }

    @Test
    public void testFindByStockLessThan() {
        int threshold = 50;
        List<Ingredient> lowStockIngredients = ingredientRepository.findByStockLessThan(threshold);
        assertEquals(0, lowStockIngredients.size());
    }

    @Test
    public void testFindBySupplierId() {
        List<Ingredient> supplierIngredients = ingredientRepository.findBySupplierId(this.testSupplierId);
        assertEquals(1, supplierIngredients.size());
    }

    @Test
    public void testFindLowStockIngredientsWithSupplier() {
        int threshold = 50;
        List<Ingredient> lowStockIngredients = ingredientRepository.findLowStockIngredientsWithSupplier(threshold);
        assertEquals(0, lowStockIngredients.size());
    }
}
