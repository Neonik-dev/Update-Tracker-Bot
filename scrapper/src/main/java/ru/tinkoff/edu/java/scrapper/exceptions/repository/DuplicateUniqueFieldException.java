package ru.tinkoff.edu.java.scrapper.exceptions.repository;

public class DuplicateUniqueFieldException extends RuntimeException {
    public DuplicateUniqueFieldException(String message) {
        super(message);
    }
}