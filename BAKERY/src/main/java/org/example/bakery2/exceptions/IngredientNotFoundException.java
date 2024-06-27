package org.example.bakery2.exceptions;

public class IngredientNotFoundException extends RuntimeException{
    public IngredientNotFoundException(String message) {
        super(message);
    }
}
