package org.example.warehouse2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

// Expliciet @Column annotatie en db kolomnaam anders mapping problemen.

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DeliveryItem implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Column (name = "ingredientnumber")
    private String ingredientNumber;
    @Column
    private int quantity;

    public DeliveryItem(String ingredientNumber, int quantity) {
        this.ingredientNumber = ingredientNumber;
        this.quantity = quantity;
    }

    @ManyToOne
    @JoinColumn(name = "delivery_id",nullable = false)
    private Delivery delivery;
}
