package ru.tinkoff.edu.java.logic.commands;

import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.tinkoff.edu.java.bot.configuration.CommandConfig;
import ru.tinkoff.edu.java.bot.logic.commands.TrackCommand;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Import(value = {CommandConfig.class})
public class TrackCommandTest extends CommandTest {
    private final TrackCommand trackCommand;
    @Test
    void callTrack_TextReply() {
        // given
        String answer = "Напишите ссылку, которую хотите начать отслеживать";

        // when
        SendMessage sendMessage = trackCommand.execute(message);

        // then
        assertAll(
                () -> assertEquals(sendMessage.getParameters().get("text"), answer),
                () -> assertEquals(sendMessage.getParameters().get("chat_id"), chat_id),
                () -> assertEquals(sendMessage.getParameters().get("reply_markup").getClass().getSimpleName(), "ForceReply")
        );
    }
}
