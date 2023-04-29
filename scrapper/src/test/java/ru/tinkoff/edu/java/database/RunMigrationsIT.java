package ru.tinkoff.edu.java.database;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RunMigrationsIT extends IntegrationEnvironment {
    @Test
    @SneakyThrows
    public void checkUpdatesDB() {
        // given
        String query = "SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'";
        ArrayList<String> rows = new ArrayList<>();

        try (Connection conn = DB_CONTAINER.createConnection("")) {
            // when
            ResultSet result = conn.createStatement().executeQuery(query);
            while(result.next()) {
                rows.add(result.getString("table_name"));
            }
        }

        // then
        assertTrue(rows.contains("chats"));
        assertTrue(rows.contains("chat_link"));
        assertTrue(rows.contains("links"));
        assertTrue(rows.contains("domains"));
    }
}
