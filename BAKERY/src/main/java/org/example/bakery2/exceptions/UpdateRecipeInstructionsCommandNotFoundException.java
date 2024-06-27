package org.example.bakery2.exceptions;

public class UpdateRecipeInstructionsCommandNotFoundException extends RuntimeException {
    public UpdateRecipeInstructionsCommandNotFoundException(String message) {
        super(message);
    }
}
