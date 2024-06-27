package org.example.warehouse2.controllers.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.warehouse2.model.IngredientStatus;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDTO {
    private Long id;
    private String ingredientNumber;
    private String name;
    private int stock;
    private IngredientStatus status;
    private Long supplierId;
    private LocalDate expirationDate;

}
