package ru.tinkoff.edu.java.scrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(ApplicationConfig.class)
public class ScrapperApplication {
    public static void main(String[] args) {
        var ctx = SpringApplication.run(ScrapperApplication.class, args);
        ApplicationConfig config = ctx.getBean(ApplicationConfig.class);
        System.out.println(config);
    }
}