package org.example.warehouse2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column (name = "ingredientnumber")
    private String ingredientNumber;
    @Column
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "orderid", referencedColumnName = "id")
    private Order order;
}
