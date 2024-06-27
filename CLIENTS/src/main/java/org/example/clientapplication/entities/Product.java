package org.example.clientapplication.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Setter
    @Column(name = "product_number")
    private UUID productNumber;
    @Setter
    @Column(name = "product_name")
    private String productName;
    @Setter
    @Column(name = "price")
    private Double price;
    @Setter
    @Column(name = "active")
    private Boolean active;

    public UUID getNumber() {
        return productNumber;
    }
}
