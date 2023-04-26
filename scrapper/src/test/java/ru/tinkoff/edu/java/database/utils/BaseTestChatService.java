package ru.tinkoff.edu.java.database.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.DBConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.db.TestConfiguration;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = {DBConfiguration.class, TestConfiguration.class, Utils.class})
public abstract class BaseTestChatService {
    private final Utils utils;
}
