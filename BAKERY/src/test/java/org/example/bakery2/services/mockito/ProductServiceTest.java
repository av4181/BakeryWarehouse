package org.example.bakery2.services.mockito;

import org.example.bakery2.entities.Product;
import org.example.bakery2.exceptions.DuplicateProductException;
import org.example.bakery2.exceptions.IncompleteException;
import org.example.bakery2.exceptions.ProductCommandNotFoundException;
import org.example.bakery2.exceptions.ProductNotActiveException;
import org.example.bakery2.mappers.ProductMapper;
import org.example.bakery2.mappers.ProductMapperImpl;
import org.example.bakery2.repositories.ProductRepository;
import org.example.bakery2.services.ProductService;
import org.example.bakery2.services.mockito.fixtures.ProductFixtures;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Spy
    private ProductMapper productMapper = new ProductMapperImpl();

    @Mock
    private RabbitTemplate rabbitTemplate;

    //TODO
    @Test
    void findAllProducts() {
    }

    @Test
    void findProduct() {
        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.of(ProductFixtures.getNewProduct()));
        Product product = productService.getProductById(1L);

        assertNotNull(product);
        assertEquals("New Product", product.getName());
        assertEquals(1L, product.getId());
        assertFalse(product.isActive());
    }

    @Test
    void createProduct() {
        Product productEntity = productMapper.convertToEntity(ProductFixtures.getCreateProductCommand());
        Mockito.when(productRepository.save(any(Product.class))).thenReturn(productEntity);

        Product product = productService.createProduct(ProductFixtures.getCreateProductCommand());

        assertEquals("new Product", product.getName());
        assertTrue(product.isActive());
    }

    @Test
    void createProductNull() {
        assertThrows(ProductCommandNotFoundException.class, () -> productService.createProduct(null));
    }

    @Test
    void createProductNoName() {
        assertThrows(IncompleteException.class, () -> productService.createProduct(ProductFixtures.getCreateProductCommandNoName()));
    }

    @Test
    void createProductWithDuplicateName() {
        Mockito.when(productRepository.existsByNameIgnoringCase(ProductFixtures.getCreateProductCommand().name())).thenReturn(true);
        assertThrows(DuplicateProductException.class, () -> productService.createProduct(ProductFixtures.getCreateProductCommand()));
    }

    @Test
    void updateProduct() {
        Product productToBeUpdated = ProductFixtures.getNewProduct();
        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.of(productToBeUpdated));

        Product productEntity = productMapper.modifyEntity(productToBeUpdated, ProductFixtures.getUpdateProductCommand());
        Mockito.when(productRepository.save(any(Product.class))).thenReturn(productEntity);

        Product product = productService.updateProduct(1L, ProductFixtures.getUpdateProductCommand());

        assertEquals("New Product name", product.getName());
        assertFalse(product.isActive());
    }

    @Test
    void updateProductNull() {
        assertThrows(ProductCommandNotFoundException.class, () -> productService.updateProduct(1L, null));
    }

    @Test
    void updateProductNoName() {
        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.of(ProductFixtures.getNewProduct()));
        assertThrows(IncompleteException.class, () -> productService.updateProduct(1L, ProductFixtures.getUpdateProductCommandNoName()));
    }

    @Test
    void updateProductDuplicateName() {
        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.of(ProductFixtures.getNewProduct()));
        Mockito.when(productRepository.existsByNameIgnoringCase(ProductFixtures.getNewProduct().getName())).thenReturn(true);
        assertThrows(DuplicateProductException.class, () -> productService.updateProduct(1L, ProductFixtures.getUpdateProductCommandDuplicateName()));
    }


    @Test
    void setProductInActive() {
        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.of(ProductFixtures.getNewProductActive()));
        Mockito.when(productRepository.save(any(Product.class))).thenReturn(ProductFixtures.getNewProduct());

        Product product = productService.deactivateProduct(1L);

        assertFalse(product.isActive());
        assertEquals("New Product", product.getName());
        assertEquals(1L, product.getId());
    }

    @Test
    void setProductActive() {
        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.of(ProductFixtures.getNewProduct()));
        assertThrows(ProductNotActiveException.class, () -> productService.deactivateProduct(1L));

    }

}