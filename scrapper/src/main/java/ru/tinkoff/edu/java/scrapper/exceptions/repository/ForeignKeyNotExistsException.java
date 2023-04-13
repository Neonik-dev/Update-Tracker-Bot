package ru.tinkoff.edu.java.scrapper.exceptions.repository;

public class ForeignKeyNotExistsException extends Exception {
    public ForeignKeyNotExistsException(String message) {
        super(message);
    }
}
