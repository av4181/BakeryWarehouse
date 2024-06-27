package org.example.warehouse2.services;

import jakarta.transaction.Transactional;
import org.example.warehouse2.controllers.api.dto.NewSupplierDTO;
import org.example.warehouse2.controllers.api.dto.SupplierDTO;
import org.example.warehouse2.exceptions.SupplierCreationException;
import org.example.warehouse2.exceptions.SupplierNotFoundException;
import org.example.warehouse2.model.Supplier;
import org.example.warehouse2.persistence.mappers.SupplierMapper;
import org.example.warehouse2.persistence.repositories.SupplierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierServiceImpl implements SupplierService {

    private static final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public SupplierServiceImpl(SupplierRepository supplierRepository, SupplierMapper supplierMapper) {
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
    }

    @Override
    public List<SupplierDTO> getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        logger.debug("Suppliers fetched from repository: {}", suppliers);

        List<SupplierDTO> supplierDTOs = suppliers.stream()
                .map(supplierMapper::toDTO)
                .collect(Collectors.toList());

        logger.debug("SupplierDTOs created: {}", supplierDTOs);
        return supplierDTOs;
    }

    @Override
    @Transactional
    public SupplierDTO createSupplier(NewSupplierDTO newSupplierDTO) {
        Supplier supplier = supplierMapper.toEntity(newSupplierDTO);
        try {
            Supplier savedSupplier = supplierRepository.save(supplier);
            return supplierMapper.toDTO(savedSupplier);
        } catch (DataAccessException e) {
            throw new SupplierCreationException("Error creating supplier: " + e.getMessage(), e);
        }
    }
    @Override
    public SupplierDTO getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found: " + id));

        return supplierMapper.toDTO(supplier);
    }
    @Override
    @Transactional
    public SupplierDTO updateSupplier(Long id, SupplierDTO supplierDTO) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found: " + id));
        supplierMapper.updateSupplierFromDTO(supplierDTO, existingSupplier);
        return supplierMapper.toDTO(supplierRepository.save(existingSupplier));
    }

    @Override
    @Transactional
    public void deleteSupplier(Long id) {
        supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found: " + id));
        supplierRepository.deleteById(id);
    }
    public List<SupplierDTO> searchSuppliersByName(String name) {
        return supplierRepository.searchByName(name).stream()
                .map(supplierMapper::toDTO)
                .collect(Collectors.toList());
    }
}
