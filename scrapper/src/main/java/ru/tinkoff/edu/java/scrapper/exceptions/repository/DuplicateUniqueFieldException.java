package ru.tinkoff.edu.java.scrapper.exceptions.repository;

public class DuplicateUniqueFieldException extends Exception{
    public DuplicateUniqueFieldException(String message) {
        super(message);
    }
}