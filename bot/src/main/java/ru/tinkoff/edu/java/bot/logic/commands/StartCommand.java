package ru.tinkoff.edu.java.bot.logic.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;

public class StartCommand implements BaseCommand{
    private final String name = "/start";
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return name + " -> регистрирует пользователя";
    }

    @Override
    public SendMessage execute(Message message) {
        System.out.println("Происходит проверка на регистрацию пользователя");
        String text = "Добро пожаловать!";
        return new SendMessage(message.chat().id(), text);
    }
}
