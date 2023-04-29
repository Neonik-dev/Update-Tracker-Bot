package ru.tinkoff.edu.java.scrapper.configuration.db;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@RequiredArgsConstructor
public class TestConfiguration {
    private final DataSource dataSource;
    @Bean
    public PlatformTransactionManager transactionManager() {
        JdbcTransactionManager transactionManager = new JdbcTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }
}
