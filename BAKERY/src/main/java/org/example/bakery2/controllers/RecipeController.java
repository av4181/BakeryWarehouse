package org.example.bakery2.controllers;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import org.example.bakery2.entities.Product;
import org.example.bakery2.entities.Recipe;
import org.example.bakery2.entities.RecipeIngredient;
import org.example.bakery2.entities.commands.ingredient.CreateIngredientCommand;
import org.example.bakery2.entities.commands.recipe.AddIngredientsToRecipeCommand;
import org.example.bakery2.entities.commands.recipe.AddInstructionsToRecipeCommand;
import org.example.bakery2.entities.commands.recipe.CreateRecipeCommand;
import org.example.bakery2.entities.commands.recipe.UpdateRecipeCommand;
import org.example.bakery2.enums.UnitOfMeasurementEnum;
import org.example.bakery2.services.IngredientService;
import org.example.bakery2.services.ProductService;
import org.example.bakery2.services.RecipeInstructionService;
import org.example.bakery2.services.RecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/recipes")
public class RecipeController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final ProductService productService;
    private final RecipeInstructionService recipeInstructionService;


    public RecipeController(RecipeService recipeService, IngredientService ingredientService, ProductService productService, RecipeInstructionService recipeInstructionService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.productService = productService;
        this.recipeInstructionService = recipeInstructionService;
    }

    @RequestMapping("/list")
    public String recipesList(Model model) {
        logger.info("Listing all recipes");
        model.addAttribute("recipes", recipeService.findAllRecipes());
        logger.info("Recipes listed successfully");
        return "recipe/recipes";
    }

    @RequestMapping("/{productId}/create")
    public String showCreateRecipeForm(@PathVariable Long productId, Model model) {
        logger.info("Displaying form to create a new recipe for product with ID: {}", productId);
        Product product = productService.getProductById(productId);
        Optional<Recipe> maybeRecipe = recipeService.findRecipeForProduct(product.getId());
        Recipe recipe = maybeRecipe.orElse(null);

        model.addAttribute("createRecipeCommand", new CreateRecipeCommand("", product));
        model.addAttribute("product", product);
        model.addAttribute("recipe", recipe);
        logger.info("Create recipe form displayed for product ID: {}", productId);
        return "recipe/createRecipe";
    }

    @PostMapping("/{productId}/create")
    public String createRecipe(@PathVariable Long productId, CreateRecipeCommand createRecipeCommand) {
        logger.info("Creating new recipe for product with ID: {}", productId);
        Recipe createdRecipe = recipeService.createRecipe(createRecipeCommand);
        Long recipeId = createdRecipe.getId();
        logger.info("New recipe created with ID: {} for product ID: {}", recipeId, productId);
        return "redirect:/recipes/" + productId + '/' + recipeId;
    }

    @GetMapping("/{productId}/{recipeId}")
    public String showRecipeDetail(@PathVariable Long productId, @PathVariable Long recipeId, Model model) {
        logger.info("Showing details for recipe with ID: {} and product ID: {}", recipeId, productId);
        Recipe recipe = recipeService.getRecipeById(recipeId);
        List<RecipeIngredient> recipeIngredients = recipeService.getRecipeIngredientsForRecipe(recipeId);
        Product product = productService.getProductById(productId);

        model.addAttribute("recipe", recipe);
        model.addAttribute("recipeIngredients", recipeIngredients);
        model.addAttribute("product", product);
        logger.info("Recipe details displayed for recipe ID: {} and product ID: {}", recipeId, productId);
        return "recipe/recipeDetail";
    }

    @GetMapping("/{productId}/{recipeId}/instructions")
    public String showAddInstructionsToRecipe(@PathVariable Long productId, @PathVariable Long recipeId, Model model) {
        logger.info("Displaying form to add instructions to recipe with ID: {}", recipeId);
        model.addAttribute("recipe", recipeService.getRecipeById(recipeId));
        model.addAttribute("addInstructionsToRecipeCommand", new AddInstructionsToRecipeCommand(""));
        model.addAttribute("product", productService.getProductById(productId));
        logger.info("Add instructions form displayed for recipe ID: {}", recipeId);
        return "recipe/addInstructionsToRecipe";
    }

    //TODO: move to recipeInstructionsController? Or not
    @PostMapping("/{recipeId}/instructions/")
    public String addInstructionsToRecipe(@PathVariable Long recipeId, AddInstructionsToRecipeCommand addInstructionsToRecipeCommand, HttpServletRequest request) {
        logger.info("Adding instructions to recipe with ID: {}", recipeId);
        recipeInstructionService.addInstructionsToRecipe(recipeId, addInstructionsToRecipeCommand);
        logger.info("Instructions added to recipe with ID: {}", recipeId);
        return "redirect:" + request.getHeader("referer");
    }

    @GetMapping("/{productId}/{recipeId}/ingredients")
    public String showAddIngredientsToRecipe(@PathVariable Long productId, @PathVariable Long recipeId, Model model) {
        logger.info("Displaying form to add ingredients to recipe with ID: {}", recipeId);
        model.addAttribute("createIngredientCommand", new CreateIngredientCommand(""));
        model.addAttribute("recipe", recipeService.getRecipeById(recipeId));
        model.addAttribute("ingredients", ingredientService.findAllIngredients());
        model.addAttribute("unitOfMeasurements", UnitOfMeasurementEnum.values());
        model.addAttribute("addIngredientsToRecipeCommand", new AddIngredientsToRecipeCommand("", "", ""));
        model.addAttribute("recipeIngredients", recipeService.getRecipeIngredientsForRecipe(recipeId));
        model.addAttribute("product", productService.getProductById(productId));
        logger.info("Add ingredients form displayed for recipe ID: {}", recipeId);
        return "recipe/addIngredientsToRecipe";
    }

    @PostMapping("/{recipeId}/ingredients/")
    public String addIngredientToRecipe(@PathVariable Long recipeId, AddIngredientsToRecipeCommand addIngredientsToRecipeCommand, HttpServletRequest request) {
        logger.info("Adding ingredient {} to recipe with ID: {}", addIngredientsToRecipeCommand.ingredient(), recipeId);
        recipeService.addIngredientToRecipe(recipeId, addIngredientsToRecipeCommand);
        logger.info("Ingredient {} added to recipe with ID: {}", addIngredientsToRecipeCommand.ingredient(), recipeId);
        return "redirect:" + request.getHeader("referer");
    }

    @PutMapping("/{productId}/{recipeId}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long productId, @PathVariable Long recipeId, @RequestBody UpdateRecipeCommand updateRecipeCommand) {
        logger.info("Updating recipe with ID: {} for product ID: {}", recipeId, productId);
        Recipe updatedRecipe = recipeService.updateRecipe(recipeId, updateRecipeCommand);
        logger.info("Recipe updated with ID: {} for product ID: {}", recipeId, productId);
        return ResponseEntity.ok(updatedRecipe);
    }

    @PatchMapping("/{recipeId}/finalize")
    public ResponseEntity<Recipe> toggleRecipeFinalizeState(@PathVariable Long recipeId) {
        logger.info("Toggling finalize state for recipe with ID: {}", recipeId);
        Recipe updatedRecipe = recipeService.finalizeRecipe(recipeId);
        logger.info("Recipe finalize state toggled for recipe with ID: {}", recipeId);

        return ResponseEntity.ok(updatedRecipe);
    }

}