package ru.tinkoff.edu.java.logic.commands;

import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.bot.logic.commands.UntrackCommand;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UntrackCommandTest extends CommandTest{
    @Test
    void callTrack_TextReply() {
        // given
        String answer = "Напишите ссылку, отслеживание которой хотите прекратить";

        // when
        SendMessage sendMessage = new UntrackCommand().execute(message);

        // then
        assertAll(
                () -> assertEquals(sendMessage.getParameters().get("text"), answer),
                () -> assertEquals(sendMessage.getParameters().get("chat_id"), chat_id),
                () -> assertEquals(sendMessage.getParameters().get("reply_markup").getClass().getSimpleName(), "ForceReply")
        );
    }
}
