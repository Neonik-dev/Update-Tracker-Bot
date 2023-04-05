package ru.tinkoff.edu.java.bot.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.logic.commands.InputHandler;

@Configuration
@RequiredArgsConstructor
public class InputHandlerConfig {
    @Bean("inputHandler")
    public InputHandler getInputHandler() {
        return new InputHandler();
    }
}
