package ru.tinkoff.edu.java.bot.logic.wrapper;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;

import java.util.List;

public interface TgBot extends AutoCloseable, UpdatesListener {
    @Override
    int process(List<Update> updates);

    void start();

    @Override
    void close();
}
