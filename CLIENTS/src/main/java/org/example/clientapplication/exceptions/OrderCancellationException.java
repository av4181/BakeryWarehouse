package org.example.clientapplication.exceptions;

public class OrderCancellationException extends RuntimeException{

    public OrderCancellationException(String message){
        super(message);
    }
}
