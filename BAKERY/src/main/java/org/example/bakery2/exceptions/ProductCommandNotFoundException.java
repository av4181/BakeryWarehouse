package org.example.bakery2.exceptions;

public class ProductCommandNotFoundException extends RuntimeException {
    public ProductCommandNotFoundException(String message) {
        super(message);
    }
}
