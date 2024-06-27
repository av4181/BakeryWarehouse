package org.example.bakery2.exceptions;

public class RecipeIngredientCommandNotFoundException extends RuntimeException{
    public RecipeIngredientCommandNotFoundException(String message) {
        super(message);
    }
}
