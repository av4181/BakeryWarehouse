package org.example.bakery2.services;

import org.example.bakery2.entities.Product;
import org.example.bakery2.entities.commands.product.CreateProductCommand;
import org.example.bakery2.entities.commands.product.UpdateProductCommand;
import org.example.bakery2.entities.dto.ProductStateDTO;
import org.example.bakery2.exceptions.DuplicateProductException;
import org.example.bakery2.exceptions.IncompleteException;
import org.example.bakery2.exceptions.ProductCommandNotFoundException;
import org.example.bakery2.exceptions.ProductNotActiveException;
import org.example.bakery2.exceptions.ProductNotFoundException;
import org.example.bakery2.mappers.ProductMapper;
import org.example.bakery2.rabbitMQ.sender.MessageSender;
import org.example.bakery2.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final RabbitTemplate rabbitTemplate;


    public ProductService(ProductRepository productRepository, ProductMapper productMapper, RabbitTemplate rabbitTemplate) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long productId) {
        Optional<Product> maybeProduct = productRepository.findById(productId);
        if (maybeProduct.isEmpty())
            throw new ProductNotFoundException(String.format("No product with id %d found", productId));

        return maybeProduct.get();
    }

    //ACTIVE = FALSE FOR NEW PRODUCTS!
    @Transactional
    public Product createProduct(CreateProductCommand createProductCommand) {
        if (createProductCommand == null)
            throw new ProductCommandNotFoundException("Create product command not found");

        if (createProductCommand.name().isEmpty())
            throw new IncompleteException("The product that was provided was incomplete. Name should not be empty or null");

        //CHECK IF PRODUCTNAME ALREADY EXISTS
        String productName = createProductCommand.name();
        if (productRepository.existsByNameIgnoringCase(productName))
            throw new DuplicateProductException(String.format("Product with name %s already exists!", productName));

        Product productEntity = productMapper.convertToEntity(createProductCommand);
        Product createdProduct = productRepository.save(productEntity);
        return createdProduct;
    }

    @Transactional
    public Product updateProduct(Long productId, UpdateProductCommand updateProductCommand) {
        if (updateProductCommand == null) {
            throw new ProductCommandNotFoundException("Update product command not found");
        }

        Optional<Product> maybeProduct = productRepository.findById(productId);
        if (maybeProduct.isEmpty()) {
            throw new ProductNotFoundException(String.format("No product with id %d found", productId));
        }

        if (updateProductCommand.name() == null || updateProductCommand.name().isEmpty()) {
            throw new IncompleteException("The product that was provided was incomplete. Name should not be empty or null");
        }

        //CHECK IF PRODUCTNAME ALREADY EXISTS
        String productName = updateProductCommand.name();
        if (productRepository.existsByNameIgnoringCase(productName))
            throw new DuplicateProductException(String.format("Product with name %s already exists!", productName));

        Product productToBeUpdated = maybeProduct.get();
        Product updatedProduct = productMapper.modifyEntity(productToBeUpdated, updateProductCommand);

        Product savedProduct = productRepository.save(updatedProduct);
        return savedProduct;
    }

    @Transactional
    public Product deactivateProduct(Long productId) {
        Optional<Product> maybeProduct = productRepository.findById(productId);
        if (maybeProduct.isEmpty())
            throw new ProductNotFoundException(String.format("No product with id %d found", productId));

        Product product = maybeProduct.get();
        if (!maybeProduct.get().isActive())
            throw new ProductNotActiveException(String.format("Product with id %d is not active!", productId));

        product.setActive(false);
        Product savedProduct = productRepository.save(product);

        ProductStateDTO productDTO = productMapper.convertToStateDTO(savedProduct);
        rabbitTemplate.convertAndSend(MessageSender.PRODUCT_ACTIVE_STATE_CHANGED, productDTO);

        return savedProduct;
    }

}


