package org.example.clientapplication.exceptions;

public class LoyaltyLevelNotFoundException extends RuntimeException {
    public LoyaltyLevelNotFoundException(String message) {
        super(message);
    }
}
