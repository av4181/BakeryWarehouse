package org.example.clientapplication.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "loyalty_level")
public class LoyaltyLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "level")
    private String level;
    @Column(name = "points")
    private int points;
    @Column(name = "discount_percentage")
    private double discountPercentage;

}
