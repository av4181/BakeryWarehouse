package org.example.warehouse2.serviceTests.mockito.fixtures;

import org.example.warehouse2.controllers.api.dto.NewSupplierDTO;
import org.example.warehouse2.controllers.api.dto.SupplierDTO;
import org.example.warehouse2.model.Supplier;

import java.util.ArrayList;
import java.util.List;

public class SupplierFixtures {

    public static Supplier createSupplier() {
        return new Supplier(null, "Supplier A", "Alice", "alice@example.com", "1234567890", new ArrayList<>());
    }

    public static SupplierDTO createSupplierDTO() {
        return new SupplierDTO(null, "Supplier A", "Alice", "alice@example.com", "1234567890");
    }

    public static NewSupplierDTO createNewSupplierDTO() {
        return new NewSupplierDTO("Supplier A", "Alice", "alice@example.com", "1234567890");
    }

    public static List<Supplier> createSupplierList() {
        return List.of(
                new Supplier(1L, "Supplier A", "Alice", "alice@example.com", "1234567890", new ArrayList<>()),
                new Supplier(2L, "Supplier B", "Bob", "bob@example.com", "9876543210", new ArrayList<>())
        );
    }
}
