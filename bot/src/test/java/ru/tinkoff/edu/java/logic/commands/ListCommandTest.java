package ru.tinkoff.edu.java.logic.commands;

import com.pengrad.telegrambot.request.SendMessage;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import ru.tinkoff.edu.java.bot.clients.ScrapperClient;
import ru.tinkoff.edu.java.bot.configuration.CommandConfig;
import ru.tinkoff.edu.java.bot.dto.scrapper.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.scrapper.ListLinksResponse;
import ru.tinkoff.edu.java.bot.logic.commands.ListCommand;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Import(value = {CommandConfig.class})
public class ListCommandTest extends CommandTest {
    @Mock
    private ScrapperClient scrapperClient;
    @InjectMocks
    private ListCommand listCommand;

    @Test
    void listLinkEmpty_SpecialText() {
        // given
        String specialText = "Пока нет ни одной отслеживаемой ссылки.\nДобавить ссылку можно с помощью команды /track";
        when(scrapperClient.getListLinks(message.chat().id()))
                .thenReturn(new ListLinksResponse(new ArrayList<>(), 0)
                );

        // when
        SendMessage sendMessage = listCommand.execute(message);

        // then
        assertMessage(sendMessage, specialText);
    }

    @Test
    @SneakyThrows
    void listLinkFull_ListLink() {
        // given
        String link1 = "https://www.baeldung.com/";
        String link2 = "https://docs.oracle.com/en/java/";
        String answer = String.format("Список всех ссылок:\n%s\n%s", link1, link2);

        when(scrapperClient.getListLinks(message.chat().id()))
                .thenReturn(
                        new ListLinksResponse(
                                Arrays.asList(
                                        new LinkResponse(1L, URI.create(link1)),
                                        new LinkResponse(2L, URI.create(link2))
                                ),
                                2
                        )
                );

        // when
        SendMessage sendMessage = listCommand.execute(message);

        // then
        assertMessage(sendMessage, answer);
    }
}
