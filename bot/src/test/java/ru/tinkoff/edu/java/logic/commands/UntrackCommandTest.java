package ru.tinkoff.edu.java.logic.commands;

import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.tinkoff.edu.java.bot.configuration.CommandConfig;
import ru.tinkoff.edu.java.bot.logic.commands.UntrackCommand;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Import(value = {CommandConfig.class})
public class UntrackCommandTest extends CommandTest {
    private final UntrackCommand untrackCommand;

    @Test
    void callTrack_TextReply() {
        // given
        String answer = "Напишите ссылку, отслеживание которой хотите прекратить";

        // when
        SendMessage sendMessage = untrackCommand.execute(message);

        // then
        assertAll(
                () -> assertEquals(sendMessage.getParameters().get("text"), answer),
                () -> assertEquals(sendMessage.getParameters().get("chat_id"), chat_id),
                () -> assertEquals(sendMessage.getParameters().get("reply_markup").getClass().getSimpleName(), "ForceReply")
        );
    }
}
