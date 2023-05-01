package ru.tinkoff.edu.java.bot.logic.wrapper;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.List;

public interface TgBot extends UpdatesListener{
    @Override
    int process(List<Update> updates);
    void start();
    void sendMessage(SendMessage message);
    void close();
}
