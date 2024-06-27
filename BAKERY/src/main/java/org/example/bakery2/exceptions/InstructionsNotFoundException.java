package org.example.bakery2.exceptions;

public class InstructionsNotFoundException extends RuntimeException {
    public InstructionsNotFoundException(String message) {
        super(message);
    }
}
