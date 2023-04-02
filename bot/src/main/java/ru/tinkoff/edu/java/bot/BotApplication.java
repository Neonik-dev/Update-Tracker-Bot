package ru.tinkoff.edu.java.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.tinkoff.edu.java.bot.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.bot.logic.TgUpdaterLinkBot;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotApplication {
    public static void main(String[] args) {
        var ctx = SpringApplication.run(BotApplication.class, args);
        ApplicationConfig config = ctx.getBean(ApplicationConfig.class);
        TgUpdaterLinkBot tgBot = ctx.getBean("TgBot", TgUpdaterLinkBot.class);
        tgBot.start();
        System.out.println(config);
    }
}
