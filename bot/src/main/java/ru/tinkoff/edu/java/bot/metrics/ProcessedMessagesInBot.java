package ru.tinkoff.edu.java.bot.metrics;

import io.micrometer.core.instrument.Metrics;

public class ProcessedMessagesInBot {
    private static final String COMPLETED_MESSAGES = "bot_completed_messages";

    private ProcessedMessagesInBot() {
    }

    public static void messageCount() {
        Metrics.counter(COMPLETED_MESSAGES).increment();
    }
}
