package ru.tinkoff.edu.java.bot.logic.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;

public interface BaseCommand {
    String getName();
    String getDescription();
    SendMessage execute(Message message);
}
