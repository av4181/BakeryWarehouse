package org.example.clientapplication.services;

import jakarta.transaction.Transactional;
import org.example.clientapplication.entities.Product;
import org.example.clientapplication.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getNewProducts() {
        return productRepository.findByPriceIsNull();
    }

    public void setProductPrice(UUID productNumber, double price) {
        Product product = productRepository.findByProductNumber(productNumber);
        if (product != null) {
            product.setPrice(price);
            product.setActive(true);
            productRepository.save(product);
        } else {
            throw new IllegalArgumentException("Product not found");
        }
    }
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void deactivateProduct(UUID productNumber) {
        Product product = productRepository.findByProductNumber(productNumber);
        if (product != null) {
            product.setActive(false);
            productRepository.save(product);
        } else {
            throw new IllegalArgumentException("Product not found");
        }
    }
}
