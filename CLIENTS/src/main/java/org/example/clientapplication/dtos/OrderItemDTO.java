package org.example.clientapplication.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.clientapplication.entities.OrderItem;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private UUID productNumber;
    private int quantity;

    public void copyFrom(OrderItem orderItem) {
        this.productNumber = orderItem.getProductNumber();
        this.quantity = orderItem.getQuantity();
    }
}
