package ru.tinkoff.edu.java.scrapper.exceptions.repository;

public class BadEntityException extends Exception{
    public BadEntityException() {
        super("В данных отсутствуют необходимые поля");
    }
    public BadEntityException(String message) {
        super(message);
    }
}
