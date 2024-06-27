package org.example.clientapplication.exceptions;

public class AccountDeletionException extends RuntimeException{

    public AccountDeletionException(String message) {
        super(message);
    }
}
