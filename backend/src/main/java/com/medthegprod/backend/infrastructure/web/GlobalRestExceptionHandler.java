package com.medthegprod.backend.infrastructure.web;

import com.medthegprod.backend.catalog.application.service.ProductNotFoundException;
import com.medthegprod.backend.identity.application.service.InvalidCredentialsException;
import com.medthegprod.backend.identity.application.service.UserAlreadyExistsException;
import com.medthegprod.backend.identity.application.service.UserSuspendedException;
import com.medthegprod.backend.library.application.service.LibraryAccessDeniedException;
import com.medthegprod.backend.sales.application.service.OrderAccessDeniedException;
import com.medthegprod.backend.sales.application.service.OrderNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalRestExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiError> handleValidation(
                        MethodArgumentNotValidException exception,
                        HttpServletRequest request) {
                Map<String, String> errors = new LinkedHashMap<>();
                for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
                        errors.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage());
                }
                return error(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR",
                                "Request validation failed", request, errors);
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ApiError> handleConstraintViolation(
                        ConstraintViolationException exception,
                        HttpServletRequest request) {
                Map<String, String> errors = new LinkedHashMap<>();
                exception.getConstraintViolations().forEach(violation -> errors
                                .put(violation.getPropertyPath().toString(), violation.getMessage()));
                return error(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR",
                                "Request validation failed", request, errors);
        }

        @ExceptionHandler({
                        HttpMessageNotReadableException.class,
                        MethodArgumentTypeMismatchException.class })
        public ResponseEntity<ApiError> handleMalformedRequest(
                        Exception exception,
                        HttpServletRequest request) {
                return error(HttpStatus.BAD_REQUEST, "INVALID_REQUEST",
                                "Request could not be read", request, Map.of());
        }

        @ExceptionHandler(NoResourceFoundException.class)
        public ResponseEntity<ApiError> handleNotFound(
                        NoResourceFoundException exception,
                        HttpServletRequest request) {
                return error(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND",
                                "Resource not found", request, Map.of());
        }

        @ExceptionHandler(ProductNotFoundException.class)
        public ResponseEntity<ApiError> handleProductNotFound(
                        ProductNotFoundException exception,
                        HttpServletRequest request) {
                return error(HttpStatus.NOT_FOUND, "PRODUCT_NOT_FOUND",
                                exception.getMessage(), request, Map.of());
        }

        @ExceptionHandler(OrderNotFoundException.class)
        public ResponseEntity<ApiError> handleOrderNotFound(
                        OrderNotFoundException exception,
                        HttpServletRequest request) {
                return error(HttpStatus.NOT_FOUND, "ORDER_NOT_FOUND",
                                exception.getMessage(), request, Map.of());
        }

        @ExceptionHandler(UserAlreadyExistsException.class)
        public ResponseEntity<ApiError> handleUserAlreadyExists(
                        UserAlreadyExistsException exception,
                        HttpServletRequest request) {
                return error(HttpStatus.CONFLICT, "USER_ALREADY_EXISTS",
                                exception.getMessage(), request, Map.of());
        }

        @ExceptionHandler({ LibraryAccessDeniedException.class, OrderAccessDeniedException.class })
        public ResponseEntity<ApiError> handleAccessDenied(
                        RuntimeException exception,
                        HttpServletRequest request) {
                String code = exception instanceof LibraryAccessDeniedException
                                ? "LIBRARY_ACCESS_DENIED"
                                : "ORDER_ACCESS_DENIED";
                return error(HttpStatus.FORBIDDEN, code, exception.getMessage(), request, Map.of());
        }

        @ExceptionHandler(InvalidCredentialsException.class)
        public ResponseEntity<ApiError> handleInvalidCredentials(
                        InvalidCredentialsException exception,
                        HttpServletRequest request) {
                return error(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS",
                                exception.getMessage(), request, Map.of());
        }

        @ExceptionHandler(UserSuspendedException.class)
        public ResponseEntity<ApiError> handleSuspendedUser(
                        UserSuspendedException exception,
                        HttpServletRequest request) {
                return error(HttpStatus.FORBIDDEN, "USER_SUSPENDED",
                                exception.getMessage(), request, Map.of());
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ApiError> handleIllegalArgument(
                        IllegalArgumentException exception,
                        HttpServletRequest request) {
                return error(HttpStatus.BAD_REQUEST, "INVALID_REQUEST",
                                exception.getMessage(), request, Map.of());
        }

        @ExceptionHandler(IllegalStateException.class)
        public ResponseEntity<ApiError> handleIllegalState(
                        IllegalStateException exception,
                        HttpServletRequest request) {
                return error(HttpStatus.BAD_REQUEST, "INVALID_PRODUCT_STATE",
                                exception.getMessage(), request, Map.of());
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiError> handleUnexpected(
                        Exception exception,
                        HttpServletRequest request) {
                return error(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR",
                                "An unexpected error occurred", request, Map.of());
        }

        private ResponseEntity<ApiError> error(
                        HttpStatus status,
                        String code,
                        String message,
                        HttpServletRequest request,
                        Map<String, String> errors) {
                return ResponseEntity.status(status).body(new ApiError(
                                OffsetDateTime.now(ZoneOffset.UTC),
                                status.value(),
                                code,
                                code,
                                message,
                                request.getRequestURI(),
                                errors));
        }

        public record ApiError(
                        OffsetDateTime timestamp,
                        int status,
                        String code,
                        String error,
                        String message,
                        String path,
                        Map<String, String> errors) {
        }
}