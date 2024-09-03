package br.com.wishlist.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    public void handleCustomException_ShouldReturnNotFound() {
        // Arrange
        CustomException ex = new CustomException("Resource not found");

        // Act
        ResponseEntity<ErrorDetails> response = exceptionHandler.handleCustomException(ex);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Resource not found", response.getBody().errors().get(0));
    }

    @Test
    public void handleGeneralException_ShouldReturnInternalServerError() {
        // Arrange
        Exception ex = new Exception("Unexpected error");

        // Act
        ResponseEntity<ErrorDetails> response = exceptionHandler.handleGeneralException(ex);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected error", response.getBody().errors().get(0));
    }
}