package com.example.store.common;

import com.example.store.product.DuplicateSkuException;
import com.example.store.product.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

  @ExceptionHandler(ProductNotFoundException.class)
  ProblemDetail handleNotFound(ProductNotFoundException ex, HttpServletRequest req) {
    log.warn("Not found: {} {} - {}", req.getMethod(), req.getRequestURI(), ex.getMessage());
    ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    pd.setTitle("Resource not found");
    pd.setType(URI.create("https://example.com/problems/not-found"));
    pd.setProperty("timestamp", Instant.now().toString());
    return pd;
  }

  @ExceptionHandler(DuplicateSkuException.class)
  ProblemDetail handleDuplicate(DuplicateSkuException ex, HttpServletRequest req) {
    log.info("Conflict: {} {} - {}", req.getMethod(), req.getRequestURI(), ex.getMessage());
    ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    pd.setTitle("Conflict");
    pd.setType(URI.create("https://example.com/problems/conflict"));
    pd.setProperty("timestamp", Instant.now().toString());
    return pd;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
    log.info("Validation failed: {} {}", req.getMethod(), req.getRequestURI());

    Map<String, String> errors = new HashMap<>();
    for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
      errors.put(fe.getField(), fe.getDefaultMessage());
    }

    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    pd.setTitle("Validation error");
    pd.setDetail("Request validation failed");
    pd.setType(URI.create("https://example.com/problems/validation"));
    pd.setProperty("errors", errors);
    pd.setProperty("timestamp", Instant.now().toString());
    return pd;
  }

  @ExceptionHandler(Exception.class)
  ProblemDetail handleGeneric(Exception ex, HttpServletRequest req) {
    log.error("Unhandled error on {} {}", req.getMethod(), req.getRequestURI(), ex);
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    pd.setTitle("Internal server error");
    pd.setDetail("An unexpected error occurred");
    pd.setType(URI.create("https://example.com/problems/internal"));
    pd.setProperty("timestamp", Instant.now().toString());
    return pd;
  }
}
