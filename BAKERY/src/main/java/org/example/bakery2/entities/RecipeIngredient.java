package org.example.bakery2.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bakery2.enums.UnitOfMeasurementEnum;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
//    @ManyToOne(fetch = FetchType.EAGER)
    private Recipe recipe;

    @ManyToOne
    private Ingredient ingredient;

    private double amount;

    private UnitOfMeasurementEnum unitOfMeasurement;

}
