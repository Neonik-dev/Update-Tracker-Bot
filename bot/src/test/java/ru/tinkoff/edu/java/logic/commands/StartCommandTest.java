package ru.tinkoff.edu.java.logic.commands;

import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.bot.logic.commands.StartCommand;

@RequiredArgsConstructor
public class StartCommandTest extends CommandTest{
    private final StartCommand startCommand;
    @Test
    void greetingText_OK() {
        // given
        String greeting = "Добро пожаловать!\nДля списка всех команд напишите команду /help";

        // when
        SendMessage sendMessage = startCommand.execute(message);

        // then
        assertMessage(sendMessage, greeting);
    }
}
