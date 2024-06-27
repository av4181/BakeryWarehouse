package org.example.bakery2.controllers;

import org.example.bakery2.entities.Product;
import org.example.bakery2.entities.Recipe;
import org.example.bakery2.entities.commands.product.CreateProductCommand;
import org.example.bakery2.entities.commands.product.UpdateProductCommand;
import org.example.bakery2.exceptions.ProductNotFoundException;
import org.example.bakery2.services.BakingService;
import org.example.bakery2.services.ProductService;
import org.example.bakery2.services.RecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;
    private final RecipeService recipeService;
    private final BakingService bakingService;

    public ProductController(ProductService productService, RecipeService recipeService, BakingService bakingService) {
        this.productService = productService;
        this.recipeService = recipeService;
        this.bakingService = bakingService;
    }

    @RequestMapping("/list")
    public String productsList(Model model) {
        logger.info("Listing all products");
        List<Product> products = productService.findAllProducts();
        Map<Long, Recipe> productRecipes = new HashMap<>();

        for (Product product : products) {
            Optional<Recipe> maybeRecipe = recipeService.findRecipeForProduct(product.getId());
            if (maybeRecipe.isPresent()) {
                productRecipes.put(product.getId(), maybeRecipe.get());
            } else {
                productRecipes.put(product.getId(), null);
            }
        }

        model.addAttribute("products", products);
        model.addAttribute("productRecipes", productRecipes);
        logger.info("Products listed successfully");
        return "product/products";
    }

    @RequestMapping("/{productId}")
    public String showProductDetail(@PathVariable Long productId, Model model) {
        logger.info("Showing details for product with ID: {}", productId);
        Product product = productService.getProductById(productId);
        Optional<Recipe> maybeRecipe = recipeService.findRecipeForProduct(product.getId());
        Recipe recipe = maybeRecipe.orElse(null);
        model.addAttribute("product", product);
        model.addAttribute("recipe", recipe);
        logger.info("Product details displayed for product ID: {}", productId);
        return "product/productDetail";
    }

    @RequestMapping("/create")
    public String showCreateProductForm(Model model) {
        logger.info("Displaying form to create a new product");
        model.addAttribute("createProductCommand", new CreateProductCommand(""));
        logger.info("Create product form displayed");
        return "product/createProduct";
    }

    @PostMapping("/create")
    public String createProduct(CreateProductCommand createProductCommand) {
        logger.info("Creating new product with name: {}", createProductCommand.name());
        Product createdProduct = productService.createProduct(createProductCommand);
        Long productId = createdProduct.getId();
        logger.info("New product created with ID: {}", productId);
        return "redirect:/products/" + productId;
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody UpdateProductCommand updateProductCommand) {
        logger.info("Updating product with ID: {}", productId);
        Product updatedProduct = productService.updateProduct(productId, updateProductCommand);
        logger.info("Product updated with ID: {}", productId);
        return ResponseEntity.ok(updatedProduct);
    }

    @PatchMapping("/{productId}/deactivate")
    public ResponseEntity<Product> deactivateProduct(@PathVariable Long productId) {
        logger.info("Trying to set state for product with ID: {} to inactive", productId);
        Product updatedProduct = productService.deactivateProduct(productId);
        logger.info("Product with ID: {} deactivated", productId);
        return ResponseEntity.ok(updatedProduct);
    }

    @PostMapping("/{productId}/bake")
    public void bakeProduct(@PathVariable Long productId) {
        Map<Product, Integer> productsToBeBaked = bakingService.getProductsToBeBaked();

        productsToBeBaked.forEach((product, quantity) -> {
            if (product.getId().equals(productId)) {
                for (int i = 0; i < quantity; i++) {
                    logger.info("Baking product with ID: {}", productId);
                    bakingService.bakeProduct(productId);
                    logger.info("Product baked with ID: {}", productId);
                }
            } else {
                throw new ProductNotFoundException(String.format("Product with ID: %s not found in the list ready to be baked", productId));
            }
        });
    }

    @RequestMapping("/bakeAll")
    public void bakeAll() {
        bakingService.bakeAllProducts();
    }

}
