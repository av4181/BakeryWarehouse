package org.example.clientapplication.rabbitMQ;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.clientapplication.dtos.ProductDto;
import org.example.clientapplication.entities.Product;
import org.example.clientapplication.repositories.ProductRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class NewProductListener {

    @Autowired
    private ProductRepository productRepository;

    @RabbitListener(queues = "new-product-queue")
    public void handleNewProductMessage(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProductDto productDto = objectMapper.readValue(message, ProductDto.class);

            // Create a new product entity
            Product product = new Product();
            product.setProductNumber(UUID.fromString(productDto.getProductNumber()));
            product.setProductName(productDto.getProductName());
            product.setPrice(null); // Blank price
            product.setActive(false);

            // Save the new product to the database
            productRepository.save(product);

            System.out.println("New product saved: " + product.getProductName());
        } catch (Exception e) {
            System.err.println("Failed to process new product message: " + e.getMessage());
        }
    }
}
