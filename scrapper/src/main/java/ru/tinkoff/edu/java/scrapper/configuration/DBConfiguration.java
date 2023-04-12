package ru.tinkoff.edu.java.scrapper.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(PostgresqlConfig.class)
public class DBConfiguration {
    private final PostgresqlConfig postgresqlConfig;

    @Bean("dataSource")
    public DataSource getDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(postgresqlConfig.url());
        hikariConfig.setUsername(postgresqlConfig.username());
        hikariConfig.setPassword(postgresqlConfig.password());

        return new HikariDataSource(hikariConfig);
    }

    @Bean("jdbcTemplate")
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }

}
