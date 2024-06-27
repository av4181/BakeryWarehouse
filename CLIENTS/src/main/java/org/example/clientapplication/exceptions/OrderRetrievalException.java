package org.example.clientapplication.exceptions;

public class OrderRetrievalException extends RuntimeException{

    public OrderRetrievalException(String message){
        super(message);
    }
}
