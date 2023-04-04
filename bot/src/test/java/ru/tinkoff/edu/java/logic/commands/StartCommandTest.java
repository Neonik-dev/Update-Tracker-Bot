package ru.tinkoff.edu.java.logic.commands;

import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.bot.logic.commands.StartCommand;

public class StartCommandTest extends CommandTest{
    @Test
    void greetingText_OK() {
        // given
        String greeting = "Добро пожаловать!\nДля списка всех команд напишите команду /help";

        // when
        SendMessage sendMessage = new StartCommand().execute(message);

        // then
        assertMessage(sendMessage, greeting);
    }
}
