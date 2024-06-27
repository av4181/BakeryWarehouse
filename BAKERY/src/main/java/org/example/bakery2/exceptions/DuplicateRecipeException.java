package org.example.bakery2.exceptions;

public class DuplicateRecipeException extends RuntimeException {
    public DuplicateRecipeException(String message) {
        super(message);
    }
}
