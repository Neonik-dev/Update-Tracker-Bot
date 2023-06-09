package ru.tinkoff.edu.java.scrapper.persistence.repository.jooq;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jooq.Converter;

import java.util.Map;

public class ConverterJson implements Converter<String, Map<String, String>> {
    @Override
    public Map<String, String> from(String jsonString) {
        return new Gson().fromJson(
                jsonString,
                new TypeToken<Map<String, String>>() {
                }.getType()
        );
    }

    @Override
    public String to(Map<String, String> dataChanges) {
        return new Gson().toJson(dataChanges);
    }

    @Override
    public @NotNull Class<String> fromType() {
        return String.class;
    }

    @Override
    public @NotNull Class toType() {
        return Map.class;
    }
}
