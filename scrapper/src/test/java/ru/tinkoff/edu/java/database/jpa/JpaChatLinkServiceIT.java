package ru.tinkoff.edu.java.database.jpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.tinkoff.edu.java.database.utils.Utils;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.configuration.DBConfiguration;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.persistence.service.jpa.JpaChatService;

@SpringBootTest(classes = ScrapperApplication.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = {Utils.class, DBConfiguration.class})
public class JpaChatLinkServiceIT {
    private final JpaChatService chatService;
    private final JpaChatRepository chatRepository;
    private final Utils utils;
    private Chat chatData;

    @BeforeEach
    public void initChatData() {
        chatData = utils.createChatData();
    }
}
