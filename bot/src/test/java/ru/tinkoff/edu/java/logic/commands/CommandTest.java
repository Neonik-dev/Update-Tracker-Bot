package ru.tinkoff.edu.java.logic.commands;

import com.google.gson.Gson;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandTest {
    protected Long chat_id;
    protected Message message;

    @BeforeEach
    protected void createMessage() {
        chat_id = new Random().nextLong();
        String json = String.format("{chat={id=%d}}", chat_id);
        message = new Gson().fromJson(json, Message.class);
    }

    protected void assertMessage(SendMessage sendMessage, String text) {
        assertAll(
                () -> assertEquals(sendMessage.getParameters().get("text"), text),
                () -> assertEquals(sendMessage.getParameters().get("chat_id"), chat_id)
        );
    }
}
