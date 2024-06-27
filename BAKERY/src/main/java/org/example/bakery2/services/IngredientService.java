package org.example.bakery2.services;

import org.example.bakery2.entities.Ingredient;
import org.example.bakery2.entities.commands.ingredient.CreateIngredientCommand;
import org.example.bakery2.exceptions.DuplicateIngredientException;
import org.example.bakery2.exceptions.IncompleteException;
import org.example.bakery2.exceptions.IngredientCommandNotFoundException;
import org.example.bakery2.exceptions.IngredientNotFoundException;
import org.example.bakery2.mappers.IngredientMapper;
import org.example.bakery2.repositories.IngredientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;

    public IngredientService(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
    }

    public List<Ingredient> findAllIngredients() {
        return ingredientRepository.findAll();
    }

    @Transactional
    public Ingredient createIngredient(CreateIngredientCommand createIngredientCommand) {
        if (createIngredientCommand == null)
            throw new IngredientCommandNotFoundException("Create ingredient command not found");

        if (createIngredientCommand.name() == null || createIngredientCommand.name().isBlank())
            throw new IncompleteException("The ingredient that was provided was incomplete. Name should not be empty or null");

        //CHECK IF INGREDIENT ALREADY EXISTS
        String ingredientName = createIngredientCommand.name();
        if (ingredientRepository.existsByNameIgnoringCase(ingredientName))
            throw new DuplicateIngredientException(String.format("Ingredient with name %s already exists!", ingredientName));

        Ingredient ingredientEntity = ingredientMapper.convertToEntity(createIngredientCommand);
        Ingredient createdIngredient = ingredientRepository.save(ingredientEntity);
        return createdIngredient;
    }

    @Transactional
    public void deleteIngredientById(Long ingredientId) {
        Optional<Ingredient> maybeIngredient = ingredientRepository.findById(ingredientId);
        if (maybeIngredient.isEmpty())
            throw new IngredientNotFoundException(String.format("No ingredient with id %d found", ingredientId));

        //TODO: check if available in recipe!

        ingredientRepository.deleteById(ingredientId);
    }

}
