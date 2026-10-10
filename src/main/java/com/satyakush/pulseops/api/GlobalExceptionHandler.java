package com.satyakush.pulseops.api;

import com.satyakush.pulseops.service.ServiceNotFoundException;
import com.satyakush.pulseops.incident.IncidentNotFoundException;
import com.satyakush.pulseops.service.DuplicateServiceNameException;
import com.satyakush.pulseops.auth.InvalidCredentialsException;
import com.satyakush.pulseops.auth.DuplicateUsernameException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceNotFoundException.class)
    public ResponseEntity<ApiError> handleServiceNotFound(ServiceNotFoundException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error(HttpStatus.NOT_FOUND, exception.getMessage(), request));
    }

    @ExceptionHandler(IncidentNotFoundException.class)
    public ResponseEntity<ApiError> handleIncidentNotFound(IncidentNotFoundException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error(HttpStatus.NOT_FOUND, exception.getMessage(), request));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
      public ResponseEntity<ApiError> handleInvalidCredentials(InvalidCredentialsException exception, HttpServletRequest request) { return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error(HttpStatus.UNAUTHORIZED, "Invalid username or password", request)); }
      @ExceptionHandler({DuplicateServiceNameException.class, DuplicateUsernameException.class})
    public ResponseEntity<ApiError> handleDuplicateService(RuntimeException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error(HttpStatus.CONFLICT, exception.getMessage(), request));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Request validation failed");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error(HttpStatus.BAD_REQUEST, message, request));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpectedException(Exception exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), request));
    }

    private ApiError error(HttpStatus status, String message, HttpServletRequest request) {
        Object requestId = request.getAttribute(RequestIdFilter.ATTRIBUTE);
        return new ApiError(
                java.time.OffsetDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                requestId == null ? null : requestId.toString()
        );
    }
}
