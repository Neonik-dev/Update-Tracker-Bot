package ru.tinkoff.edu.java.scrapper.exceptions.repository;

public class InvalidLinkException extends RuntimeException {
    public InvalidLinkException(String message) {
        super((message));
    }
}
