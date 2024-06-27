package org.example.warehouse2.exceptions;

public class SupplierCreationException extends RuntimeException {

    public SupplierCreationException(String message) {
        super(message);
    }

    public SupplierCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
