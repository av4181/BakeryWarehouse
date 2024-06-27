package org.example.bakery2.exceptions.handler;

import org.example.bakery2.exceptions.AddInstructionsToRecipeCommandNotFoundException;
import org.example.bakery2.exceptions.DuplicateIngredientException;
import org.example.bakery2.exceptions.DuplicateProductException;
import org.example.bakery2.exceptions.DuplicateRecipeException;
import org.example.bakery2.exceptions.DuplicateRecipeIngredientException;
import org.example.bakery2.exceptions.ErrorMessage;
import org.example.bakery2.exceptions.IncompleteException;
import org.example.bakery2.exceptions.IngredientNotFoundException;
import org.example.bakery2.exceptions.InstructionsNotFoundException;
import org.example.bakery2.exceptions.ProductCommandNotFoundException;
import org.example.bakery2.exceptions.ProductNotActiveException;
import org.example.bakery2.exceptions.ProductNotFoundException;
import org.example.bakery2.exceptions.RecipeCommandNotFoundException;
import org.example.bakery2.exceptions.RecipeFinalizedException;
import org.example.bakery2.exceptions.RecipeIngredientCommandNotFoundException;
import org.example.bakery2.exceptions.RecipeIngredientNotFoundException;
import org.example.bakery2.exceptions.RecipeNotFoundException;
import org.example.bakery2.exceptions.UpdateRecipeInstructionsCommandNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class ControllerExceptionHandler {

    private ResponseEntity<ErrorMessage> handleException(HttpStatus status, RuntimeException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                status.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(message, status);
    }

    /*
     **************************************************
     * Not Found Exceptions
     **************************************************
     */

    @ExceptionHandler(ProductCommandNotFoundException.class)
    public ResponseEntity<ErrorMessage> productCommandNotFoundException(ProductCommandNotFoundException ex, WebRequest request) {
        return handleException(HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler(RecipeCommandNotFoundException.class)
    public ResponseEntity<ErrorMessage> recipeCommandNotFoundException(RecipeCommandNotFoundException ex, WebRequest request) {
        return handleException(HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<ErrorMessage> recipeNotFoundException(RecipeNotFoundException ex, WebRequest request) {
        return handleException(HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorMessage> productNotFoundException(ProductNotFoundException ex, WebRequest request) {
        return handleException(HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler(RecipeIngredientNotFoundException.class)
    public ResponseEntity<ErrorMessage> recipeIngredientNotFoundException(RecipeIngredientNotFoundException ex, WebRequest request) {
        return handleException(HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler(RecipeIngredientCommandNotFoundException.class)
    public ResponseEntity<ErrorMessage> recipeIngredientCommandNotFoundException(RecipeIngredientCommandNotFoundException ex, WebRequest request) {
        return handleException(HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler(IngredientNotFoundException.class)
    public ResponseEntity<ErrorMessage> ingredientNotFoundException(IngredientNotFoundException ex, WebRequest request) {
        return handleException(HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler(UpdateRecipeInstructionsCommandNotFoundException.class)
    public ResponseEntity<ErrorMessage> recipeInstructionsCommandNotFoundException(UpdateRecipeInstructionsCommandNotFoundException ex, WebRequest request) {
        return handleException(HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler(InstructionsNotFoundException.class)
    public ResponseEntity<ErrorMessage> instructionsNotFoundException(InstructionsNotFoundException ex, WebRequest request) {
        return handleException(HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler(AddInstructionsToRecipeCommandNotFoundException.class)
    public ResponseEntity<ErrorMessage> addInstructionsToRecipeCommandNotFoundException(AddInstructionsToRecipeCommandNotFoundException ex, WebRequest request) {
        return handleException(HttpStatus.NOT_FOUND, ex, request);
    }

    /*
     **************************************************
     * Expectation Failed Exceptions
     **************************************************
     */

    @ExceptionHandler(IncompleteException.class)
    public ResponseEntity<ErrorMessage> incompleteException(IncompleteException ex, WebRequest request) {
        return handleException(HttpStatus.EXPECTATION_FAILED, ex, request);
    }

    /*
     **************************************************
     * Duplicate Exceptions
     **************************************************
     */

    @ExceptionHandler(DuplicateRecipeIngredientException.class)
    public ResponseEntity<ErrorMessage> duplicateRecipeIngredientException(DuplicateRecipeIngredientException ex, WebRequest request) {
        return handleException(HttpStatus.CONFLICT, ex, request);
    }

    @ExceptionHandler(DuplicateIngredientException.class)
    public ResponseEntity<ErrorMessage> duplicateIngredientException(DuplicateIngredientException ex, WebRequest request) {
        return handleException(HttpStatus.CONFLICT, ex, request);
    }

    @ExceptionHandler(DuplicateProductException.class)
    public ResponseEntity<ErrorMessage> duplicateProductException(DuplicateProductException ex, WebRequest request) {
        return handleException(HttpStatus.CONFLICT, ex, request);
    }

    @ExceptionHandler(DuplicateRecipeException.class)
    public ResponseEntity<ErrorMessage> duplicateRecipeException(DuplicateRecipeException ex, WebRequest request) {
        return handleException(HttpStatus.CONFLICT, ex, request);
    }

    /*
     **************************************************
     * Forbidden Exceptions
     **************************************************
     */

    @ExceptionHandler(RecipeFinalizedException.class)
    public ResponseEntity<ErrorMessage> recipeFinalizedException(RecipeFinalizedException ex, WebRequest request) {
        return handleException(HttpStatus.FORBIDDEN, ex, request);
    }

    @ExceptionHandler(ProductNotActiveException.class)
    public ResponseEntity<ErrorMessage> productNotActiveException(ProductNotActiveException ex, WebRequest request) {
        return handleException(HttpStatus.FORBIDDEN, ex, request);
    }


}
