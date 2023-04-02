package ru.tinkoff.edu.java.bot.logic.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Getter;

@Getter
public class StartCommand implements BaseCommand{
    private static final String GREETING = "Добро пожаловать!\nДля списка всех команд напишите команду /help";

    @Override
    public SendMessage execute(Message message) {
        System.out.println("Происходит проверка на регистрацию пользователя");
        return new SendMessage(message.chat().id(), GREETING);
    }
}
