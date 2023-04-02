package ru.tinkoff.edu.java.bot.dto.api;


public record ApiErrorResponse(
        String description,
        String code,
        String exceptionName,
        String exceptionMessage,

        StackTraceElement[] stackTrace) {
}
