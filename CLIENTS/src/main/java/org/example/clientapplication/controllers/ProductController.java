package org.example.clientapplication.controllers;

import org.example.clientapplication.dtos.ProductDto;
import org.example.clientapplication.entities.Product;
import org.example.clientapplication.mappers.ProductMapper;
import org.example.clientapplication.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    private ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping("/new")
    //TODO : voorzien dat enkel client_admin hier aan kan
    public ResponseEntity<List<ProductDto>> getNewProducts() {
        try {
            List<Product> list = productService.getNewProducts();
            return ResponseEntity.ok(list.stream().map(productMapper::toDto).toList());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/setPrice")
    //TODO : voorzien dat enkel client_admin hier aan kan
    public ResponseEntity<String> setProductPrice(UUID productNumber, double price) {
        try {
            productService.setProductPrice(productNumber, price);
            return ResponseEntity.ok("Price set successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/deactivate")
    //TODO : voorzien dat enkel client_admin of systeem hier aan kan
    public ResponseEntity<String> deactivateProduct(UUID productNumber) {
        try {
            productService.deactivateProduct(productNumber);
            return ResponseEntity.ok("Product deactivated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        try {
            List<Product> list = productService.getAllProducts();
            return ResponseEntity.ok(list.stream().map(productMapper::toDto).toList());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
