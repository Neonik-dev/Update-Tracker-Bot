package ru.tinkoff.edu.java.logic.commands;

import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.bot.logic.commands.TrackCommand;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrackCommandTest extends CommandTest {
    @Test
    void callTrack_TextReply() {
        // given
        String answer = "Напишите ссылку, которую хотите начать отслеживать";

        // when
        SendMessage sendMessage = new TrackCommand().execute(message);

        // then
        assertAll(
                () -> assertEquals(sendMessage.getParameters().get("text"), answer),
                () -> assertEquals(sendMessage.getParameters().get("chat_id"), chat_id),
                () -> assertEquals(sendMessage.getParameters().get("reply_markup").getClass().getSimpleName(), "ForceReply")
        );
    }
}
