package org.example.warehouse2.serviceTests.mockito;

import org.example.warehouse2.controllers.api.dto.NewSupplierDTO;
import org.example.warehouse2.controllers.api.dto.SupplierDTO;
import org.example.warehouse2.exceptions.SupplierNotFoundException;
import org.example.warehouse2.persistence.mappers.SupplierMapper;
import org.example.warehouse2.persistence.mappers.SupplierMapperImpl;
import org.example.warehouse2.persistence.repositories.SupplierRepository;
import org.example.warehouse2.serviceTests.mockito.fixtures.SupplierFixtures;
import org.example.warehouse2.services.SupplierServiceImpl;
import org.example.warehouse2.model.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Fail.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class SupplierServiceImplTest {

    @Mock
    private SupplierRepository supplierRepository;
    @Spy
    private SupplierMapper supplierMapper = new SupplierMapperImpl();
    @InjectMocks
    private SupplierServiceImpl supplierService;

    @Test
    void testGetAllSuppliers() {
        List<Supplier> mockSuppliers = SupplierFixtures.createSupplierList();
        Mockito.lenient().when(supplierRepository.findAll()).thenReturn(mockSuppliers);
        Mockito.lenient().when(supplierMapper.toDTO(any(Supplier.class))).thenReturn(SupplierFixtures.createSupplierDTO());

        List<SupplierDTO> result = supplierService.getAllSuppliers();

        assertEquals(2, result.size());
        assertEquals("Supplier A", result.get(0).getName());
        assertEquals("Supplier B", result.get(1).getName());
    }

    @Test
    void testGetSupplierById_Found() {
        Supplier mockSupplier = SupplierFixtures.createSupplier();
        SupplierDTO mockSupplierDTO = SupplierFixtures.createSupplierDTO();
        Long supplierId = mockSupplier.getId(); // id van fixture

        Mockito.lenient().when(supplierRepository.findById(supplierId)).thenReturn(Optional.of(mockSupplier));
        Mockito.lenient().when(supplierMapper.toDTO(mockSupplier)).thenReturn(mockSupplierDTO); // Return originele DTO

        SupplierDTO result = supplierService.getSupplierById(supplierId);

        assertEquals(mockSupplierDTO, result);
    }

    @Test
    void testGetSupplierById_NotFound() {
        Long supplierId = 1L;
        Mockito.lenient().when(supplierRepository.findById(supplierId)).thenReturn(Optional.empty());

        assertThrows(SupplierNotFoundException.class, () -> supplierService.getSupplierById(supplierId));
    }

    @Test
    void testCreateSupplier() {
        NewSupplierDTO newSupplierDTO = SupplierFixtures.createNewSupplierDTO();
        Supplier supplierToPersist = supplierMapper.toEntity(newSupplierDTO);
        Supplier persistedSupplier = new Supplier(null, "New Supplier", "John Smith", "john.smith@example.com", "555-1234", new ArrayList<>());
        SupplierDTO expectedSupplierDTO = SupplierFixtures.createSupplierDTO();

        Mockito.lenient().when(supplierRepository.save(supplierToPersist)).thenReturn(persistedSupplier);
        Mockito.lenient().when(supplierMapper.toDTO(persistedSupplier)).thenReturn(expectedSupplierDTO);

        SupplierDTO createdSupplier = supplierService.createSupplier(newSupplierDTO);

        assertEquals(expectedSupplierDTO, createdSupplier);
        verify(supplierRepository).save(supplierToPersist);
    }

    @Test
    void testUpdateSupplier_ExistingSupplier() throws SupplierNotFoundException {
        Supplier existingSupplier = SupplierFixtures.createSupplier();
        SupplierDTO updatedSupplierDTO = SupplierFixtures.createSupplierDTO();
        updatedSupplierDTO.setName("Updated Supplier Name");

        Mockito.lenient().when(supplierRepository.findById(existingSupplier.getId())).thenReturn(Optional.of(existingSupplier));
        Mockito.lenient().when(supplierRepository.save(any(Supplier.class))).thenAnswer(invocation -> invocation.getArgument(0)); //updated supplier

        SupplierDTO result = supplierService.updateSupplier(existingSupplier.getId(), updatedSupplierDTO);

        assertEquals(updatedSupplierDTO.getName(), result.getName()); // Verificatie
        verify(supplierRepository).save(existingSupplier);
    }

    @Test
    void testUpdateSupplier_SupplierNotFound() {
        Long nonExistentSupplierId = 999L;
        SupplierDTO updatedSupplierDTO = SupplierFixtures.createSupplierDTO();

        Mockito.lenient().when(supplierRepository.findById(nonExistentSupplierId)).thenReturn(Optional.empty());

        assertThrows(SupplierNotFoundException.class, () -> supplierService.updateSupplier(nonExistentSupplierId, updatedSupplierDTO));
        verify(supplierRepository, never()).save(any());
    }

    @Test
    void testDeleteSupplier_ExistingSupplier() {
        Supplier existingSupplier = SupplierFixtures.createSupplier();

        Mockito.lenient().when(supplierRepository.findById(existingSupplier.getId())).thenReturn(Optional.of(existingSupplier));

        supplierService.deleteSupplier(existingSupplier.getId());

        verify(supplierRepository).deleteById(existingSupplier.getId());

    }

    @Test
    void testDeleteSupplier_SupplierNotFound() {
        Long nonExistentSupplierId = 999L;

        Mockito.lenient().when(supplierRepository.findById(nonExistentSupplierId)).thenReturn(Optional.empty());

        assertThrows(SupplierNotFoundException.class, () -> supplierService.deleteSupplier(nonExistentSupplierId));
        verify(supplierRepository, never()).deleteById(any());
    }

    @Test
    void testSearchSuppliersByName() {
        String searchTerm = "lier";
        List<Supplier> mockSuppliers = SupplierFixtures.createSupplierList();

        Mockito.lenient().when(supplierRepository.searchByName(searchTerm)).thenReturn(mockSuppliers);
        Mockito.lenient().when(supplierMapper.toDTO(any(Supplier.class))).thenReturn(SupplierFixtures.createSupplierDTO());

        List<SupplierDTO> result = supplierService.searchSuppliersByName(searchTerm);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(dto -> dto.getName().contains(searchTerm)));
    }
}
