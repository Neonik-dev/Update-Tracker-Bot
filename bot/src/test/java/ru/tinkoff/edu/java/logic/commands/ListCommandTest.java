package ru.tinkoff.edu.java.logic.commands;

import com.pengrad.telegrambot.request.SendMessage;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.java.bot.logic.commands.ListCommand;
import ru.tinkoff.edu.java.bot.logic.utils.ManagerCollection;

import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public class ListCommandTest extends CommandTest {
    @Test
    void listLinkEmpty_SpecialText() {
        // given
        String specialText = "Пока нет ни одной отслеживаемой ссылки.\nДобавить ссылку можно с помощью команды /track";

        // when
        SendMessage sendMessage = new ListCommand().execute(message);

        // then
        assertMessage(sendMessage, specialText);
    }

    @Test
    @SneakyThrows
    void listLinkFull_ListLink() {
        // given
        String answer = "Список всех ссылок:\nhttps://www.baeldung.com/\nhttps://docs.oracle.com/en/java/";
        try(MockedStatic<ManagerCollection> managerCollection = mockStatic(ManagerCollection.class)) {
            managerCollection.when(ManagerCollection::getLinks).thenReturn(new HashSet<>(Arrays.asList(
                    URI.create("https://www.baeldung.com/"),
                    URI.create("https://docs.oracle.com/en/java/")
            )));

            // when
            SendMessage sendMessage = new ListCommand().execute(message);

            // then
            assertMessage(sendMessage, answer);
        }
    }
}
