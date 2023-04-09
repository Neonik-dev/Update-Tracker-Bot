package ru.tinkoff.edu.java.bot.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.tinkoff.edu.java.bot.dto.api.ApiErrorResponse;


@RestControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String DESCRIPTION_400 = HttpStatus.BAD_REQUEST.getReasonPhrase();
    private static final String STATUS_CODE_400 = String.valueOf(HttpStatus.BAD_REQUEST.value());

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.badRequest().body(new ApiErrorResponse(
                DESCRIPTION_400,
                STATUS_CODE_400,
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                ex.getStackTrace()
        ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ApiErrorResponse> handlerInvalidRequestParameters(MethodArgumentTypeMismatchException exception) {
        return ResponseEntity.badRequest().body(new ApiErrorResponse(
                DESCRIPTION_400,
                STATUS_CODE_400,
                exception.getClass().getSimpleName(),
                exception.getMessage(),
                exception.getStackTrace()
        ));
    }
}
