package ru.tinkoff.edu.java.bot.responses;


public record ApiErrorResponse(
        String description,
        String code,
        String exceptionName,
        String exceptionMessage,

        StackTraceElement[] stackTrace) {
}
