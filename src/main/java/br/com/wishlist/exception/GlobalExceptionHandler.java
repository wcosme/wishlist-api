package br.com.wishlist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorDetails> handleCustomException(CustomException ex) {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                ex.getCode(),
                "Error",
                Collections.singletonList(ex.getMessage())
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.valueOf(ex.getCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGeneralException(Exception ex) {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                Collections.singletonList(ex.getMessage())
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}