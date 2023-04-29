package ru.tinkoff.edu.java.database;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.DirectoryResourceAccessor;
import liquibase.resource.ResourceAccessor;
import lombok.SneakyThrows;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;

@Testcontainers
public abstract class IntegrationEnvironment {
    private static final Path SCRAPPER_PATH = new File(".").toPath().toAbsolutePath().getParent();
    protected static final PostgreSQLContainer<?> DB_CONTAINER;

    static {
        DB_CONTAINER = new PostgreSQLContainer<>("postgres:15")
                .withDatabaseName("scrapper")
                .withUsername("tinkoff")
                .withPassword("he_is_the_only_one");
        DB_CONTAINER.start();
        runMigrations();
    }

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasourse.url", DB_CONTAINER::getJdbcUrl);
        registry.add("spring.datasourse.username", DB_CONTAINER::getUsername);
        registry.add("spring.datasourse.password", DB_CONTAINER::getPassword);
    }

    @SneakyThrows
    private static void runMigrations() {
        try (Connection conn = DB_CONTAINER.createConnection("")) {
            PostgresDatabase database = new PostgresDatabase();
            database.setConnection(new JdbcConnection(conn));

            ResourceAccessor changelogDir = new DirectoryResourceAccessor(SCRAPPER_PATH.getParent().resolve("migrations"));
            Liquibase liquibase = new Liquibase("master.xml", changelogDir, database);
            liquibase.update(new Contexts(), new LabelExpression());
        }
    }
}
