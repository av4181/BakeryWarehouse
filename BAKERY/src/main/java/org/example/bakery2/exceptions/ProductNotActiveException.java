package org.example.bakery2.exceptions;

public class ProductNotActiveException extends RuntimeException {
    public ProductNotActiveException(String message) {
        super(message);
    }
}
