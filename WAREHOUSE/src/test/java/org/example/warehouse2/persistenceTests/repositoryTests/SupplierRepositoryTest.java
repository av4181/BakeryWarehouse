package org.example.warehouse2.persistenceTests.repositoryTests;

import jakarta.transaction.Transactional;
import org.example.warehouse2.model.Ingredient;
import org.example.warehouse2.model.Supplier;
import org.example.warehouse2.persistence.mappers.SupplierMapper;
import org.example.warehouse2.persistence.repositories.SupplierRepository;
import org.example.warehouse2.serviceTests.mockito.fixtures.SupplierFixtures;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class SupplierRepositoryTest {

    @Mock
    private SupplierRepository supplierRepository;
    @Mock
    private SupplierMapper supplierMapper;

    private Long supplierId;

//    @BeforeEach
//    public void setup() {
//        Supplier supplier = new Supplier();
//        supplier.setId(1L);
//        supplier.setName("Acme Ingredients");
//        supplier.setContactPerson("Alice Smith");
//        supplier.setEmail("alice@acme.com");
//        supplier.setPhoneNumber("123-456-7890");
//
//        Supplier savedSupplier = new Supplier();
//
//        when(supplierRepository.save(supplier)).thenAnswer(invocation -> {
//            savedSupplier.setId(1L);
//            savedSupplier.setName(supplier.getName());
//            savedSupplier.setContactPerson(supplier.getContactPerson());
//            savedSupplier.setEmail(supplier.getEmail());
//            savedSupplier.setPhoneNumber(supplier.getPhoneNumber());
//            return savedSupplier;
//        });
//
//        supplierRepository.save(supplier);
//        supplierId = savedSupplier.getId();
//    }
//
//    @AfterEach
//    public void teardown() {
//        supplierRepository.deleteById(supplierId);
//    }

    @Test
    public void testFindByName() {
        Supplier supplierEntity = supplierMapper.toEntity(SupplierFixtures.createNewSupplierDTO());
        Mockito.when(supplierRepository.save(any(Supplier.class))).thenReturn(supplierEntity);

        Optional<Supplier> foundSupplier = supplierRepository.findByName("Supplier A");
        assertTrue(foundSupplier.isPresent());
        assertEquals("Supplier A", foundSupplier.get().getName());
    }

    @Test
    public void testFindByContactPerson() {
        Supplier supplierEntity = supplierMapper.toEntity(SupplierFixtures.createNewSupplierDTO());
        Mockito.when(supplierRepository.save(any(Supplier.class))).thenReturn(supplierEntity);

        List<Supplier> suppliers = supplierRepository.findByContactPerson("Alice");
        assertFalse(suppliers.isEmpty());
        assertEquals("Acme Ingredients", suppliers.get(0).getName());
    }

    @Test
    public void testFindByNameContainingIgnoreCaseAndContactPersonContainingIgnoreCase() {
        Supplier supplierEntity = supplierMapper.toEntity(SupplierFixtures.createNewSupplierDTO());
        Mockito.when(supplierRepository.save(any(Supplier.class))).thenReturn(supplierEntity);

        List<Supplier> suppliers = supplierRepository.findByNameContainingIgnoreCaseAndContactPersonContainingIgnoreCase("Acme", "alice");
        assertFalse(suppliers.isEmpty());
        assertEquals("Acme Ingredients", suppliers.get(0).getName());
    }

    @Test
    public void testExistsById() {
        assertTrue(supplierRepository.existsById(supplierId));
        assertFalse(supplierRepository.existsById(99999999L)); // Bestaat niet
    }

    @Test
    public void testSearchByName() {
        Supplier supplierEntity = supplierMapper.toEntity(SupplierFixtures.createNewSupplierDTO());
        Mockito.when(supplierRepository.save(any(Supplier.class))).thenReturn(supplierEntity);

        List<Supplier> suppliers = supplierRepository.searchByName("me");
        assertFalse(suppliers.isEmpty());
        assertEquals("Acme Ingredients", suppliers.get(0).getName());
    }
}

