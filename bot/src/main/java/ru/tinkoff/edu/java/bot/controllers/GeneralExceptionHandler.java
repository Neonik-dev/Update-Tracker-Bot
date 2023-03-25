package ru.tinkoff.edu.java.bot.controllers;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.tinkoff.edu.java.bot.dto.ApiErrorResponse;


@RestControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NotNull HttpMessageNotReadableException ex, @NotNull HttpHeaders headers, @NotNull HttpStatusCode status, @NotNull WebRequest request) {
        return ResponseEntity.badRequest().body(new ApiErrorResponse(
                "Invalid parameters in request",
                status.toString(),
                "HttpMessageNotReadableException",
                ex.getMessage(),
                ex.getStackTrace()
        ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ApiErrorResponse> handlerInvalidRequestParameters(MethodArgumentTypeMismatchException exception) {

        return ResponseEntity.badRequest().body(new ApiErrorResponse(
                "Invalid request parameters",
                "400",
                "MethodArgumentTypeMismatchException",
                exception.getMessage(),
                exception.getStackTrace()
        ));
    }
}
