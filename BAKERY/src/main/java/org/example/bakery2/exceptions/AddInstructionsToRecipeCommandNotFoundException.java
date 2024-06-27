package org.example.bakery2.exceptions;

public class AddInstructionsToRecipeCommandNotFoundException extends RuntimeException {
    public AddInstructionsToRecipeCommandNotFoundException(String message) {
        super(message);
    }
}
