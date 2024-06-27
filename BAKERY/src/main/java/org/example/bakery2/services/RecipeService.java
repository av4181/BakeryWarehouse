package org.example.bakery2.services;

import org.example.bakery2.entities.Ingredient;
import org.example.bakery2.entities.Product;
import org.example.bakery2.entities.Recipe;
import org.example.bakery2.entities.RecipeIngredient;
import org.example.bakery2.entities.commands.recipe.AddIngredientsToRecipeCommand;
import org.example.bakery2.entities.commands.recipe.CreateRecipeCommand;
import org.example.bakery2.entities.commands.recipe.UpdateRecipeCommand;
import org.example.bakery2.entities.dto.ProductDTO;
import org.example.bakery2.exceptions.DuplicateRecipeException;
import org.example.bakery2.exceptions.DuplicateRecipeIngredientException;
import org.example.bakery2.exceptions.IncompleteException;
import org.example.bakery2.exceptions.IngredientNotFoundException;
import org.example.bakery2.exceptions.ProductNotFoundException;
import org.example.bakery2.exceptions.RecipeCommandNotFoundException;
import org.example.bakery2.exceptions.RecipeFinalizedException;
import org.example.bakery2.exceptions.RecipeIngredientNotFoundException;
import org.example.bakery2.exceptions.RecipeNotFoundException;
import org.example.bakery2.mappers.ProductMapper;
import org.example.bakery2.mappers.RecipeIngredientMapper;
import org.example.bakery2.mappers.RecipeMapper;
import org.example.bakery2.rabbitMQ.sender.MessageSender;
import org.example.bakery2.repositories.RecipeIngredientRepository;
import org.example.bakery2.repositories.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private static final Logger logger = LoggerFactory.getLogger(RecipeService.class);

    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeMapper recipeMapper;
    private final RecipeIngredientMapper recipeIngredientMapper;
    private final IngredientService ingredientService;
    private final RabbitTemplate rabbitTemplate;
    private final ProductMapper productMapper;

    public RecipeService(RecipeRepository recipeRepository, RecipeIngredientRepository recipeIngredientRepository, RecipeMapper recipeMapper, RecipeIngredientMapper recipeIngredientMapper, IngredientService ingredientService, RabbitTemplate rabbitTemplate, ProductMapper productMapper) {
        this.recipeRepository = recipeRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeMapper = recipeMapper;
        this.recipeIngredientMapper = recipeIngredientMapper;
        this.ingredientService = ingredientService;
        this.rabbitTemplate = rabbitTemplate;
        this.productMapper = productMapper;
    }

    public List<Recipe> findAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe getRecipeById(Long recipeId) {
        Optional<Recipe> maybeRecipe = recipeRepository.findById(recipeId);
        if (maybeRecipe.isEmpty())
            throw new RecipeNotFoundException(String.format("No recipe with id %d found", recipeId));

        return maybeRecipe.get();
    }


    //FINALIZED = FALSE FOR NEW RECIPES
    @Transactional
    public Recipe createRecipe(CreateRecipeCommand createRecipeCommand) {
        if (createRecipeCommand == null)
            throw new RecipeCommandNotFoundException("Create recipe command not found");

        if (createRecipeCommand.name() == null || createRecipeCommand.name().isBlank())
            throw new IncompleteException("The recipe that was provided was incomplete. Name should not be empty or null");

        if (createRecipeCommand.product() == null)
            throw new ProductNotFoundException("The recipe that was provided was incomplete. Product should not be null");

        //CHECK IF RECIPENAME ALREADY EXISTS
        String recipeName = createRecipeCommand.name();
        if (recipeRepository.existsByNameIgnoringCase(recipeName))
            throw new DuplicateRecipeException(String.format("Recipe with name %s already exists!", recipeName));

        Recipe recipeEntity = recipeMapper.convertToEntity(createRecipeCommand);
        Recipe createdRecipe = recipeRepository.save(recipeEntity);

        return createdRecipe;
    }

    @Transactional
    public void addIngredientToRecipe(Long recipeId, AddIngredientsToRecipeCommand addIngredientsToRecipeCommand) {
        if (addIngredientsToRecipeCommand == null)
            throw new RecipeCommandNotFoundException("Add ingredients to recipe command not found");

        Optional<Recipe> maybeRecipe = recipeRepository.findById(recipeId);
        if (maybeRecipe.isEmpty())
            throw new RecipeNotFoundException(String.format("No recipe with id %d found", recipeId));

        //FINALIZED RECIPES CANT BE EDITED.
        if (maybeRecipe.get().isFinalized()) {
            throw new RecipeFinalizedException(String.format("Recipe with id %d is finalized and cannot be modified", recipeId));
        }

        Ingredient ingredient = ingredientService.findAllIngredients().stream()
                .filter(i -> i.getName().equals(addIngredientsToRecipeCommand.ingredient()))
                .findAny()
                .orElseThrow(() -> new IngredientNotFoundException(String.format("Ingredient with name %s not found", addIngredientsToRecipeCommand.ingredient())));

        //CHECK IF INGREDIENT ALREADY EXISTS
        //TODO: enough to check for recipe and name: there wont be any double amounts or units rite? there cant be multiple different things for the same ingredient
        if (recipeIngredientRepository.existsByRecipeAndIngredient(maybeRecipe.get(), ingredient))
            throw new DuplicateRecipeIngredientException(String.format("Ingredient with name %s already exists for recipe %s!", ingredient.getName(), maybeRecipe.get().getName()));

        RecipeIngredient recipeIngredient = recipeIngredientMapper.convertToEntity(addIngredientsToRecipeCommand);

        //MANUALLY SET RECIPE AND INGREDIENT
        recipeIngredient.setRecipe(maybeRecipe.get());
        recipeIngredient.setIngredient(ingredient);

        recipeIngredientRepository.save(recipeIngredient);
    }

    @Transactional
    public Recipe updateRecipe(Long recipeId, UpdateRecipeCommand updateRecipeCommand) {
        if (updateRecipeCommand == null) {
            throw new RecipeCommandNotFoundException("Update recipe command not found");
        }

        Optional<Recipe> maybeRecipe = recipeRepository.findById(recipeId);
        if (maybeRecipe.isEmpty())
            throw new RecipeNotFoundException(String.format("No recipe with Id %d found", recipeId));

        if (maybeRecipe.get().isFinalized()) {
            throw new RecipeFinalizedException(String.format("Recipe with id %d is finalized and cannot be modified", recipeId));
        }

        if (updateRecipeCommand.name() == null || updateRecipeCommand.name().isBlank()) {
            throw new IncompleteException("The recipe name that was provided was incomplete. Name should not be empty or null");
        }

        //CHECK IF RECIPENAME ALREADY EXISTS
        String recipeName = updateRecipeCommand.name();
        if (recipeRepository.existsByNameIgnoringCase(recipeName))
            throw new DuplicateRecipeException(String.format("Recipe with name %s already exists!", recipeName));

        Recipe recipeToBeUpdated = maybeRecipe.get();
        Recipe updatedRecipe = recipeMapper.modifyEntity(recipeToBeUpdated, updateRecipeCommand);

        Recipe savedRecipe = recipeRepository.save(updatedRecipe);

        return savedRecipe;
    }

    public List<RecipeIngredient> getRecipeIngredientsForRecipe(Long recipeId) {
        Optional<Recipe> maybeRecipe = recipeRepository.findById(recipeId);
        if (maybeRecipe.isEmpty())
            throw new RecipeNotFoundException(String.format("No recipe with id %d found", recipeId));

        Optional<List<RecipeIngredient>> maybeRecipeIngredientList = recipeIngredientRepository.findAllByRecipeId(recipeId);
        if (maybeRecipeIngredientList.isEmpty())
            throw new RecipeIngredientNotFoundException(String.format("No recipe ingredients found for recipe with id %d", recipeId));

        //TODO logging for empty ingredienstsList?
        return maybeRecipeIngredientList.get();
    }

    //TODO momenteel nog ingredients en geen recipe ingredietns, maar mss wel recipe ingredients nodig om bij de warehouse de amount te kunnen doorgeven?
    public List<Ingredient> findIngredientsForProduct(Product product) {
        if (product == null)
            throw new ProductNotFoundException("Product not found");

        Optional<Recipe> maybeRecipe = findRecipeForProduct(product.getId());
        if (maybeRecipe.isEmpty())
            throw new RecipeNotFoundException(String.format("No recipe found for product with id %d", product.getId()));

        List<RecipeIngredient> recipeIngredientList = getRecipeIngredientsForRecipe(maybeRecipe.get().getId());

        //TODO extract to helper?
        List<Ingredient> ingredientList = recipeIngredientList.stream().map(RecipeIngredient::getIngredient).collect(Collectors.toList());

        return ingredientList;
    }

    @Transactional
    public Recipe finalizeRecipe(Long recipeId) {
        Optional<Recipe> maybeRecipe = recipeRepository.findById(recipeId);
        if (maybeRecipe.isEmpty())
            throw new RecipeNotFoundException(String.format("No recipe with id %d found", recipeId));

        Recipe recipeToBeFinalized = maybeRecipe.get();
        recipeToBeFinalized.setFinalized(true);

        Recipe finalizedRecipe = recipeRepository.save(recipeToBeFinalized);

        Product product = finalizedRecipe.getProduct();
        List<Ingredient> ingredientsForProductList = findIngredientsForProduct(product);
        ProductDTO newProductDTO = productMapper.convertToDTO(product, ingredientsForProductList);
        rabbitTemplate.convertAndSend(MessageSender.NEW_PRODUCT_QUEUE, newProductDTO);
        logger.info("Message send for new product created: {}", newProductDTO.getProductName());

        return finalizedRecipe;
    }

//    public void deleteRecipeById(Long recipeId) {
//        Optional<Recipe> maybeRecipe = recipeRepository.findById(recipeId);
//        if (maybeRecipe.isEmpty())
//            throw new RecipeNotFoundException(String.format("No recipe with id %d found", recipeId));
//
//        recipeRepository.deleteById(recipeId);
//    }

    //return optional instead of throw because of frontend problems
    public Optional<Recipe> findRecipeForProduct(Long productId) {
        Optional<Recipe> maybeRecipe = recipeRepository.findByProductId(productId);
//        if (maybeRecipe.isEmpty())
//            throw new RecipeNotFoundException(String.format("No recipe for product with id %d found", productId));

        return maybeRecipe;
    }

}
