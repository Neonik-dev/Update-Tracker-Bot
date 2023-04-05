package ru.tinkoff.edu.java.logic.commands;

import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.bot.logic.commands.HelpCommand;
import ru.tinkoff.edu.java.bot.logic.commands.enums.InitBaseCommands;

import java.util.Arrays;


public class HelpCommandTest extends CommandTest {
    @Test
    void descriptionCommands_EnumBaseCommandDescription() {
        // given
        StringBuilder text = new StringBuilder("Список всех команд:");
        Arrays.stream(InitBaseCommands.values()).forEach((value) -> text.append("\n").append(value));

        // when
        SendMessage sendMessage = new HelpCommand().execute(message);

        // then
        assertMessage(sendMessage, text.toString());

    }

}
