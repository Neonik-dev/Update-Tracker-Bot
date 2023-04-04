package ru.tinkoff.edu.java.logic.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.java.bot.logic.commands.ListCommand;
import com.google.gson.Gson;
import ru.tinkoff.edu.java.bot.logic.utils.ManagerCollection;

import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public class ListCommandIT {
    private Long chat_id;
    private Message message;

    @BeforeEach
    public void createMessage() {
        chat_id = new Random().nextLong();
        String json = String.format("{chat={id=%d}}", chat_id);
        message = new Gson().fromJson(json, Message.class);
    }
    @Test
    void listLinkEmpty_SpecialText() {
        // given
        String specialText = "Пока нет ни одной отслеживаемой ссылки.\nДобавить ссылку можно с помощью команды /track";

        // when
        SendMessage sendMessage = new ListCommand().execute(message);

        // then
        assertAll(
                () -> assertEquals(sendMessage.getParameters().get("text"), specialText),
                () -> assertEquals(sendMessage.getParameters().get("chat_id"), chat_id)
        );
    }

    @Test
    @SneakyThrows
    void listLinkFull_ListLink() {
        // given
        try(MockedStatic<ManagerCollection> managerCollection = mockStatic(ManagerCollection.class)) {
            managerCollection.when(ManagerCollection::getLinks).thenReturn(new HashSet<>(Arrays.asList(
                    URI.create("link1"),
                    URI.create("link3")
            )));
            String listLink = "Список всех ссылок:\nlink1\nlink3";

            // when
            SendMessage sendMessage = new ListCommand().execute(message);

            // then
            assertAll(
                    () -> assertEquals(sendMessage.getParameters().get("text"), listLink),
                    () -> assertEquals(sendMessage.getParameters().get("chat_id"), chat_id)
            );
        }
    }
}
