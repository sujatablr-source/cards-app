package com.vs3.card.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle duplicate key or integrity constraint violations
    @ExceptionHandler({DataIntegrityViolationException.class, SQLException.class})
    public ResponseEntity<Object> handleDataIntegrityViolation(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Duplicate Card");
        body.put("message", extractMessage(ex));
        
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    // Generic unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String extractMessage(Exception ex) {
        if (ex.getCause() != null) {
            return ex.getCause().getMessage();
        }
        return ex.getMessage();
    }
}
