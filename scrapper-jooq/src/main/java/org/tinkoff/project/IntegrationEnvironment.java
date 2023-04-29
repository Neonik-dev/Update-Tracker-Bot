package org.tinkoff.project;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.sql.SQLException;


public abstract class IntegrationEnvironment {

    protected static final PostgreSQLContainer<?> DB_CONTAINER;
    private static final Path CHANGELOG_PATH = new File("migrations").toPath().toAbsolutePath();

    static {
        DB_CONTAINER = new PostgreSQLContainer<>("postgres:15")
                .withDatabaseName("scrapper")
                .withUsername("postgres")
                .withPassword("password");
        DB_CONTAINER.start();

        runMigrations();
    }

    private static void runMigrations() {
        try (var conn = DB_CONTAINER.createConnection("")) {
            var changeLogDir = new DirectoryResourceAccessor(CHANGELOG_PATH);

            var db = new PostgresDatabase();
            db.setConnection(new JdbcConnection(conn));

            var liquibase = new Liquibase("master.xml", changeLogDir, db);
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (SQLException | LiquibaseException | FileNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }
}