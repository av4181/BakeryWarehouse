package org.example.clientapplication.exceptions;

public class OrderConfirmationException extends RuntimeException{

    public OrderConfirmationException(String message){
        super(message);
    }
}
