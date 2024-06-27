package org.example.bakery2.exceptions;

public class IncompleteException extends RuntimeException {
    public IncompleteException(String message) {
        super(message);
    }
    //TODO do we make seperate for these? like recipeIncomplete, productIncomplete etc.
}
