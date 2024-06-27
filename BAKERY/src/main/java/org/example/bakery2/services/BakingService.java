package org.example.bakery2.services;

import lombok.Getter;
import org.example.bakery2.entities.Product;
import org.example.bakery2.entities.Recipe;
import org.example.bakery2.entities.RecipeIngredient;
import org.example.bakery2.entities.dto.RecipeIngredientDTO;
import org.example.bakery2.exceptions.ProductNotActiveException;
import org.example.bakery2.exceptions.ProductNotFoundException;
import org.example.bakery2.exceptions.RecipeIngredientNotFoundException;
import org.example.bakery2.exceptions.RecipeNotFoundException;
import org.example.bakery2.mappers.RecipeIngredientMapper;
import org.example.bakery2.rabbitMQ.sender.MessageSender;
import org.example.bakery2.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Getter
public class BakingService {

    private static final Logger logger = LoggerFactory.getLogger(BakingService.class);

    private final RabbitTemplate rabbitTemplate;
    private final ProductRepository productRepository;
    private final RecipeService recipeService;
    private final RecipeIngredientMapper recipeIngredientMapper;

    public BakingService(RabbitTemplate rabbitTemplate, ProductRepository productRepository, RecipeService recipeService, RecipeIngredientMapper recipeIngredientMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.productRepository = productRepository;
        this.recipeService = recipeService;
        this.recipeIngredientMapper = recipeIngredientMapper;
    }

    private final Map<Product, Integer> productsToBeBaked = new HashMap<>();

    public void addProductsToBakeList(Product product, int quantity) {
        productsToBeBaked.put(product, quantity);
    }

    public void removeProductFromBakeList(Long productId) {
        productsToBeBaked.entrySet().removeIf(entry -> entry.getKey().getId().equals(productId));
    }


    public void bakeProduct(Long productId) {
        Optional<Product> maybeProduct = productRepository.findById(productId);
        if (maybeProduct.isEmpty())
            throw new ProductNotFoundException(String.format("No product with id %d found", productId));

        if (!maybeProduct.get().isActive())
            throw new ProductNotActiveException(String.format("Product with id %d is not active and cannot be baked", productId));

        //bake the product
        rabbitTemplate.convertAndSend(MessageSender.PRODUCT_BAKED, productId);
        logger.info("Message send for product baked: {}", maybeProduct.get().getName());

        //send out ingredients order: recipe ingredients because we need ingredient (with uuid) + amount + unitOfEnum
        Optional<Recipe> maybeRecipe = recipeService.findRecipeForProduct(maybeProduct.get().getId());
        if (maybeRecipe.isEmpty())
            throw new RecipeNotFoundException(String.format("No recipe for product with id %d found", maybeProduct.get().getId()));

        List<RecipeIngredient> recipeIngredientsForProduct = recipeService.getRecipeIngredientsForRecipe(maybeRecipe.get().getId());
        if (recipeIngredientsForProduct.isEmpty())
            throw new RecipeIngredientNotFoundException(String.format("No recipe ingredients found for recipe with id %d", maybeRecipe.get().getId()));

        List<RecipeIngredientDTO> recipeIngredientDTOList = recipeIngredientMapper.convertToDTO(recipeIngredientsForProduct);

        rabbitTemplate.convertAndSend(MessageSender.PRODUCT_INGREDIENTS_ORDER, recipeIngredientDTOList);
        for (RecipeIngredientDTO ingredientDTO : recipeIngredientDTOList) {
            logger.info("Message send for ingredient to be ordered: {}", ingredientDTO.getIngredient().getName());
        }

        removeProductFromBakeList(maybeProduct.get().getId());
    }

    public void bakeAllProducts() {
        if (!productsToBeBaked.isEmpty()) {
            for (Map.Entry<Product, Integer> entry : productsToBeBaked.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();

                for (int i = 0; i < quantity; i++) {
                    try {
                        bakeProduct(product.getId());
                    } catch (Exception e) {
                        logger.error("Failed to bake product with ID: {}. Reason: {}", product.getId(), e.getMessage());
                    }
                }
            }
        } else {
            logger.info("There is nothing to be baked!");
        }
    }
}
