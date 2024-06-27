package org.example.clientapplication.serviceTests;

import org.example.clientapplication.entities.Product;
import org.example.clientapplication.repositories.ProductRepository;
import org.example.clientapplication.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private UUID validUUID;
    private Product product;
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        validUUID = UUID.randomUUID();
        product = Product.builder().productNumber(validUUID).build();
    }

    @Test
    void testSetProductPrice_ProductExists(){
        double price = 10.0;
        when(productRepository.findByProductNumber(validUUID)).thenReturn(product);

        productService.setProductPrice(validUUID, price);

        verify(productRepository, times(1)).save(product);
        assertEquals(price, product.getPrice());
        assertTrue(product.getActive());
    }

    @Test
    void testSetProductPrice_ProductDoesNotExist_ThrowsException(){

        UUID invalidUuid = UUID.randomUUID();
        when(productRepository.findByProductNumber(invalidUuid)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.setProductPrice(invalidUuid, 10.0);
        });

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void testGetNewProducts_ReturnsProductsWithNullPrice(){
        Product product1 = Product.builder().productName("Appeltaart").productNumber(UUID.randomUUID()).price(null).build();
        Product product2 = Product.builder().productName("Koffiekoek").productNumber(UUID.randomUUID()).price(null).build();

        List<Product> expectedProducts = List.of(product1, product2);
        when(productRepository.findByPriceIsNull()).thenReturn(expectedProducts);

        List<Product> result = productService.getNewProducts();

        assertEquals(expectedProducts, result);
        verify(productRepository, times(1)).findByPriceIsNull();
    }

    @Test
    void testDeactivateProduct_ProductExists(){
        when(productRepository.findByProductNumber(validUUID)).thenReturn(product);

        productService.deactivateProduct(validUUID);

        verify(productRepository, times(1)).save(product);
        assertFalse(product.getActive());
    }

    @Test
    void testDeactivateProduct_ProductDoesNotExist_ThrowsException(){
        UUID invalidUuid = UUID.randomUUID();
        when(productRepository.findByProductNumber(invalidUuid)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.deactivateProduct(invalidUuid);
        });

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void testGetAllProducts_ReturnsNonEmptyList(){
        Product product1 = Product.builder().productName("Appeltaart").productNumber(UUID.randomUUID()).price(5.0).build();
        Product product2 = Product.builder().productName("Koffiekoek").productNumber(UUID.randomUUID()).price(3.0).build();

        List<Product> expectedProducts = List.of(product1, product2);
        when(productRepository.findAll()).thenReturn(expectedProducts);

        List<Product> result = productService.getAllProducts();

        assertEquals(expectedProducts, result);
        verify(productRepository, times(1)).findAll();
    }

}
