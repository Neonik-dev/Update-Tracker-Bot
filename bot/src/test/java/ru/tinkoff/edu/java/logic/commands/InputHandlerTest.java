package ru.tinkoff.edu.java.logic.commands;

import com.google.gson.Gson;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.bot.logic.commands.InputHandler;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InputHandlerTest {
    @Test
    void unknownCommand_MessageUnknownCommand() {
        // given
        String answer = "Такой команды не существует.\nПопробуйте воспользоваться командой /help";
        Long chat_id = new Random().nextLong();
        String json = String.format("{message={chat={id=%d}, text=randomText}}", chat_id);
        Update update = new Gson().fromJson(json, Update.class);

        // when
        SendMessage sendMessage = new InputHandler().run(update);

        // then
        assertAll(
                () -> assertEquals(sendMessage.getParameters().get("text"), answer),
                () -> assertEquals(sendMessage.getParameters().get("chat_id"), chat_id)
        );
    }
}
