package org.example.warehouse2.persistenceTests.mapperTests;

import org.example.warehouse2.controllers.api.dto.NewSupplierDTO;
import org.example.warehouse2.controllers.api.dto.SupplierDTO;
import org.example.warehouse2.model.Supplier;
import org.example.warehouse2.persistence.mappers.SupplierMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SupplierMapperTest {

    private final SupplierMapper supplierMapper = Mappers.getMapper(SupplierMapper.class);

    @Test
    public void testToDTO() {
        Supplier supplier = new Supplier(1L, "Test Supplier", "John Doe", "john.doe@example.com", "1234567890", new ArrayList<>());

        SupplierDTO supplierDTO = supplierMapper.toDTO(supplier);

        assertEquals(supplier.getId(), supplierDTO.getId());
        assertEquals(supplier.getName(), supplierDTO.getName());
        assertEquals(supplier.getContactPerson(), supplierDTO.getContactPerson());
        assertEquals(supplier.getEmail(), supplierDTO.getEmail());
        assertEquals(supplier.getPhoneNumber(), supplierDTO.getPhoneNumber());
    }

    @Test
    public void testToEntity() {
        NewSupplierDTO newSupplierDTO = new NewSupplierDTO("Test Supplier", "John Doe", "john.doe@example.com", "1234567890");

        Supplier supplier = supplierMapper.toEntity(newSupplierDTO);

        assertNull(supplier.getId());
        assertEquals(newSupplierDTO.getName(), supplier.getName());
        assertEquals(newSupplierDTO.getContactPerson(), supplier.getContactPerson());
        assertEquals(newSupplierDTO.getEmail(), supplier.getEmail());
        assertEquals(newSupplierDTO.getPhoneNumber(), supplier.getPhoneNumber());
    }
    @Test
    public void testUpdateSupplierFromDTO() {
        Supplier existingSupplier = new Supplier(1L, "Old Supplier", "Jane Smith", "jane.smith@example.com", "9876543210", new ArrayList<>());
        SupplierDTO updatedSupplierDTO = new SupplierDTO(1L, "Updated Supplier", "John Doe", "john.doe@example.com", "1234567890");

        supplierMapper.updateSupplierFromDTO(updatedSupplierDTO, existingSupplier);

        assertEquals(updatedSupplierDTO.getId(), existingSupplier.getId());
        assertEquals(updatedSupplierDTO.getName(), existingSupplier.getName());
        assertEquals(updatedSupplierDTO.getContactPerson(), existingSupplier.getContactPerson());
        assertEquals(updatedSupplierDTO.getEmail(), existingSupplier.getEmail());
        assertEquals(updatedSupplierDTO.getPhoneNumber(), existingSupplier.getPhoneNumber());
    }

    @Test
    public void testToNewSupplierDTO() {
        Supplier supplier = new Supplier(1L, "Test Supplier", "John Doe", "john.doe@example.com", "1234567890", new ArrayList<>());

        NewSupplierDTO newSupplierDTO = supplierMapper.toNewSupplierDTO(supplier);
        assertEquals(supplier.getName(), newSupplierDTO.getName());
        assertEquals(supplier.getContactPerson(), newSupplierDTO.getContactPerson());
        assertEquals(supplier.getEmail(), newSupplierDTO.getEmail());
        assertEquals(supplier.getPhoneNumber(), newSupplierDTO.getPhoneNumber());
    }
}