package ru.tinkoff.edu.java.scrapper.persistence.entity.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.google.gson.Gson;

public class ConvertorGitHub {
    private final static Gson GSON = new Gson();

    public String convertToDatabaseColumn(String dataJson) {
        System.out.println(GSON.toJson(dataJson));
        return GSON.toJson(dataJson);
    }

    public GitHubJson convertToEntityAttribute(String dbData) {
        return GSON.fromJson(dbData, GitHubJson.class);
    }
}
