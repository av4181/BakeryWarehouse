package org.example.bakery2.exceptions;

public class RecipeIngredientNotFoundException extends RuntimeException{
    public RecipeIngredientNotFoundException(String message) {
        super(message);
    }
}
