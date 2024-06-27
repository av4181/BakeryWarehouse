package org.example.bakery2.rabbitMQ.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bakery2.entities.Product;
import org.example.bakery2.entities.dto.OrderItemDTO;
import org.example.bakery2.exceptions.ProductNotFoundException;
import org.example.bakery2.repositories.ProductRepository;
import org.example.bakery2.services.BakingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageListener.class);

    private final ProductRepository productRepository;
    private final BakingService bakingService;

    public MessageListener(ProductRepository productRepository, BakingService bakingService) {
        this.productRepository = productRepository;
        this.bakingService = bakingService;
    }

    @RabbitListener(queues = "orderQueue")
    public void handleOrderMessage(String message) throws JsonProcessingException {
        //receives a list of <OrderItemDTO>
        ObjectMapper objectMapper = new ObjectMapper();
        List<OrderItemDTO> orderItemDTOList = objectMapper.readValue(message, new TypeReference<List<OrderItemDTO>>() {
        });

        //set a map of products + amount to be baked
        for (OrderItemDTO orderItemDTO : orderItemDTOList) {
            Optional<Product> maybeProduct = productRepository.findByUuid(UUID.fromString(orderItemDTO.getProductNumber()));
            if (maybeProduct.isEmpty()) {
                throw new ProductNotFoundException(String.format("Product with UUID %s not found", orderItemDTO.getProductNumber()));
            }
            logger.info("Product added to baking list: {}", maybeProduct.get().getName());
            bakingService.addProductsToBakeList(maybeProduct.get(), orderItemDTO.getQuantity());
        }

    }
}
