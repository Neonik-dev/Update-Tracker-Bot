package ru.tinkoff.edu.java.scrapper.persistence.service;

public interface ChatService {
        void register(Long tgChatId);
        void unregister(Long tgChatId);
}
