package com.satyakush.pulseops.api;

import com.satyakush.pulseops.service.ServiceNotFoundException;
import com.satyakush.pulseops.service.DuplicateServiceNameException;
import com.satyakush.pulseops.auth.InvalidCredentialsException;
import com.satyakush.pulseops.auth.DuplicateUsernameException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceNotFoundException.class)
    public ApiError handleServiceNotFound(ServiceNotFoundException exception, HttpServletRequest request) {
        return error(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
      public ApiError handleInvalidCredentials(InvalidCredentialsException exception, HttpServletRequest request) { return error(HttpStatus.UNAUTHORIZED, "Invalid username or password", request); }
      @ExceptionHandler({DuplicateServiceNameException.class, DuplicateUsernameException.class})
    public ApiError handleDuplicateService(RuntimeException exception, HttpServletRequest request) {
        return error(HttpStatus.CONFLICT, exception.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiError handleValidation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Request validation failed");

        return error(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(Exception.class)
    public ApiError handleUnexpectedException(Exception exception, HttpServletRequest request) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), request);
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
