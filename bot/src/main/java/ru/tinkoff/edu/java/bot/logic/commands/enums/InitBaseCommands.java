package ru.tinkoff.edu.java.bot.logic.commands.enums;

import lombok.Getter;
import ru.tinkoff.edu.java.bot.logic.commands.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum InitBaseCommands {
    START("/start", "регистрирует пользователя", new StartCommand()),
    HELP("/help", "выводит все доступные команды", new HelpCommand()),
    LIST("/list", "выводит список отслеживающих ссылок", new ListCommand()),
    TRACK("/track", "начинает отслеживание ссылки", new TrackCommand()),
    UNTRACK("/untrack", "прекращает отслеживание ссылки", new UntrackCommand());

    private final String name;
    private final String description;
    private final BaseCommand command;


    private static final class GuaranteeInit {
        static final Map<String, BaseCommand> COMMANDS = new HashMap<>();
    }

    static {
        Arrays.stream(values()).forEach((value) -> GuaranteeInit.COMMANDS.put(value.name, value.command));
    }


    InitBaseCommands(String name, String description, BaseCommand command) {
        this.name = name;
        this.command = command;
        this.description = description;
    }

    public static Map<String, BaseCommand> getCommands() {
        return GuaranteeInit.COMMANDS;
    }

    @Override
    public String toString() {
        return name + " -> " + description;
    }
}
