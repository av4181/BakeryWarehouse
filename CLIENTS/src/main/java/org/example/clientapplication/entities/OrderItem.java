package org.example.clientapplication.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Setter
    private Order order;

    @ManyToOne
    @Setter
    private Product product;
    @Setter
    private int quantity;

    public UUID getProductNumber() {
        return product.getNumber();
    }
}
