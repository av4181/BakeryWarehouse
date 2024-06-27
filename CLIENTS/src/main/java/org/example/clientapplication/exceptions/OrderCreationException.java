package org.example.clientapplication.exceptions;

public class OrderCreationException extends RuntimeException{

    public OrderCreationException(String message) {
        super(message);
    }
}
