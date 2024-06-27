package org.example.bakery2.exceptions;

public class DuplicateRecipeIngredientException extends RuntimeException{
    public DuplicateRecipeIngredientException(String message) {
        super(message);
    }
}
