package org.example.warehouse2.services;

import org.example.warehouse2.controllers.api.dto.NewSupplierDTO;
import org.example.warehouse2.controllers.api.dto.SupplierDTO;
import org.example.warehouse2.exceptions.SupplierNotFoundException;

import java.util.List;

public interface SupplierService {
    List<SupplierDTO> getAllSuppliers();

    SupplierDTO getSupplierById(Long id) throws SupplierNotFoundException;

    SupplierDTO createSupplier(NewSupplierDTO supplierDTO);

    SupplierDTO updateSupplier(Long id, SupplierDTO supplierDTO) throws SupplierNotFoundException;

    void deleteSupplier(Long id) throws SupplierNotFoundException;
}
