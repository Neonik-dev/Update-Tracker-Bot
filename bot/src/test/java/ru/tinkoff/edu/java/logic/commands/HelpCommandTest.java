package ru.tinkoff.edu.java.logic.commands;

import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.tinkoff.edu.java.bot.configuration.CommandConfig;
import ru.tinkoff.edu.java.bot.logic.commands.BaseCommand;
import ru.tinkoff.edu.java.bot.logic.commands.HelpCommand;
import ru.tinkoff.edu.java.bot.logic.commands.InitCommands;

import java.util.Map;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Import(value = {CommandConfig.class})
public class HelpCommandTest extends CommandTest {
    @Test
    void descriptionCommands_EnumBaseCommandDescription() {
        // given
        Map<String, BaseCommand> commands = InitCommands.getAllCommands();
        StringBuilder text = new StringBuilder("Список всех команд:");
        commands.keySet().forEach(
                (value) -> text.append("\n").append(commands.get(value).getDescription())
        );

        // when
        SendMessage sendMessage = new HelpCommand().execute(message);

        // then
        assertMessage(sendMessage, text.toString());

    }

}
