package ru.tinkoff.edu.java.scrapper.exceptions.repository;

public class EmptyResultException extends RuntimeException  {
    public EmptyResultException(String message) {
        super(message);
    }
}
