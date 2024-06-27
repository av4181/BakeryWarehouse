package org.example.warehouse2.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.warehouse2.controllers.api.dto.IngredientDTO;
import org.example.warehouse2.exceptions.IngredientNotFoundException;
import org.example.warehouse2.exceptions.SupplierNotFoundException;
import org.example.warehouse2.model.*;
import org.example.warehouse2.persistence.mappers.IngredientMapper;
import org.example.warehouse2.persistence.repositories.IngredientRepository;
import org.example.warehouse2.persistence.repositories.SupplierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final Logger logger = LoggerFactory.getLogger(IngredientServiceImpl.class);
    private final IngredientRepository ingredientRepository;
    private final SupplierRepository supplierRepository;
    private final IngredientMapper ingredientMapper;

    public IngredientServiceImpl(IngredientRepository ingredientRepository,
                                 SupplierRepository supplierRepository,
                                 IngredientMapper ingredientMapper) {
        this.ingredientRepository = ingredientRepository;
        this.supplierRepository = supplierRepository;
        this.ingredientMapper = ingredientMapper;
    }

    @Override
    public List<IngredientDTO> getIngredients() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        return ingredients.stream()
                .map(ingredientMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Ingredient getIngredientByNumber(String ingredientNumber) throws IngredientNotFoundException {
        return ingredientRepository.findByIngredientNumber(ingredientNumber)
                .orElseThrow(() -> new IngredientNotFoundException("Ingredient not found: " + ingredientNumber));
    }

    @Override
    @Transactional
    public Ingredient updateIngredient(String ingredientNumber, IngredientDTO ingredientDTO) throws IngredientNotFoundException {
        Ingredient existingIngredient = getIngredientByNumber(ingredientNumber);
        ingredientMapper.updateIngredientFromDTO(ingredientDTO, existingIngredient);

        return ingredientRepository.save(existingIngredient);
    }

    @Override
    @Transactional
    public void deleteIngredient(String ingredientNumber) throws IngredientNotFoundException {
        Ingredient ingredientToDelete = getIngredientByNumber(ingredientNumber);
        ingredientRepository.delete(ingredientToDelete);
    }

    @Override
    public void processNewIngredients(List<Ingredient> newIngredients) {
        newIngredients.forEach(ingredientRepository::save);
    }
    @Override
    public void setSupplierForIngredient(String ingredientNumber, Long supplierId) throws IngredientNotFoundException, SupplierNotFoundException  {
        if (!ingredientRepository.existsByIngredientNumber(ingredientNumber)) {
            throw new IngredientNotFoundException("Ingredient not found: " + ingredientNumber);
        }
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found: " + supplierId));

        Ingredient ingredient = ingredientRepository.findByIngredientNumber(ingredientNumber).get();
        ingredient.setSupplier(supplier);
        ingredientRepository.save(ingredient);
    }

    @Override
    public List<IngredientDTO> getIngredientsExpiringBefore(LocalDate expirationDate) {
        List<Ingredient> expiringIngredients = ingredientRepository.findByExpirationDateBefore(expirationDate);
        return expiringIngredients.stream()
                .map(ingredientMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<IngredientDTO> getLowStockIngredients() {
        int lowStockThreshold = 10;
        List<Ingredient> lowStockIngredients = ingredientRepository.findByStockLessThan(lowStockThreshold);
        return lowStockIngredients.stream()
                .map(ingredientMapper::toDTO)
                .collect(Collectors.toList());
    }
}
