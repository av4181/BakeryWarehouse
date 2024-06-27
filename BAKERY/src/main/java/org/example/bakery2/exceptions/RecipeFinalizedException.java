package org.example.bakery2.exceptions;

public class RecipeFinalizedException extends RuntimeException {
    public RecipeFinalizedException(String message) {
        super(message);
    }
}
