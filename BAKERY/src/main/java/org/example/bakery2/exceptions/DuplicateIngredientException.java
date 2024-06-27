package org.example.bakery2.exceptions;

public class DuplicateIngredientException extends RuntimeException {
    public DuplicateIngredientException(String message) {
        super(message);
    }
}
