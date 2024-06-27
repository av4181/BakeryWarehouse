package org.example.bakery2.exceptions;

public class IngredientCommandNotFoundException extends RuntimeException {
    public IngredientCommandNotFoundException(String message) {
        super(message);
    }
}
