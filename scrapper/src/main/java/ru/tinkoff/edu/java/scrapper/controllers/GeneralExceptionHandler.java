package ru.tinkoff.edu.java.scrapper.controllers;

import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.tinkoff.edu.java.scrapper.dto.ApiErrorResponse;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.BadEntityException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.DuplicateUniqueFieldException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.EmptyResultException;
import ru.tinkoff.edu.java.scrapper.exceptions.repository.ForeignKeyNotExistsException;

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

    @ExceptionHandler({BadEntityException.class, DuplicateUniqueFieldException.class, MethodArgumentTypeMismatchException.class, EmptyResultException.class, ForeignKeyNotExistsException.class})
    protected ResponseEntity<ApiErrorResponse> handlerInvalidRequestParameters(Exception exception) {
        return ResponseEntity.badRequest().body(new ApiErrorResponse(
                DESCRIPTION_400,
                STATUS_CODE_400,
                exception.getClass().getSimpleName(),
                exception.getMessage(),
                exception.getStackTrace()
        ));
    }
}
