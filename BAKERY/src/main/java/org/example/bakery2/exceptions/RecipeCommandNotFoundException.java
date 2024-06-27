package org.example.bakery2.exceptions;

public class RecipeCommandNotFoundException extends RuntimeException{
    public RecipeCommandNotFoundException(String message) {
        super(message);
    }
}
