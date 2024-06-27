package org.example.bakery2.entities.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.bakery2.entities.Ingredient;

import java.util.List;

//TODO do we need getter?
@Getter
@Setter
public class ProductDTO {

    private Long id;
    private String productNumber;
    private String productName;
    private Boolean active;
    private List<Ingredient> ingredientList;

}
