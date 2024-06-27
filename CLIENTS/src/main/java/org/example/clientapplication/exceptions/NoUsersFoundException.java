package org.example.clientapplication.exceptions;

public class NoUsersFoundException extends RuntimeException{
    public NoUsersFoundException(String message) {
        super(message);
    }
}
