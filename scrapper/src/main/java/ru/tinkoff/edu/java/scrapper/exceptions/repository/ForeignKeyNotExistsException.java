package ru.tinkoff.edu.java.scrapper.exceptions.repository;

public class ForeignKeyNotExistsException extends RuntimeException {
    public ForeignKeyNotExistsException(String message) {
        super(message);
    }
}
