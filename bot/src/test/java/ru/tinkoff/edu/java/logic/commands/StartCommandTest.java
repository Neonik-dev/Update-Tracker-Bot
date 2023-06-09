package ru.tinkoff.edu.java.logic.commands;

import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import ru.tinkoff.edu.java.bot.clients.ScrapperClient;
import ru.tinkoff.edu.java.bot.configuration.CommandConfig;
import ru.tinkoff.edu.java.bot.logic.commands.StartCommand;

import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
@Import(value = {CommandConfig.class})
public class StartCommandTest extends CommandTest{
    @Mock
    private ScrapperClient scrapperClient;
    @InjectMocks
    private StartCommand startCommand;
    @Test
    void greetingText_OK() {
        // given
        String greeting = "Добро пожаловать!\nДля списка всех команд напишите команду /help";
        doNothing().when(scrapperClient).postChatId(message.chat().id());

        // when
        SendMessage sendMessage = startCommand.execute(message);

        // then
        assertMessage(sendMessage, greeting);
    }
}
