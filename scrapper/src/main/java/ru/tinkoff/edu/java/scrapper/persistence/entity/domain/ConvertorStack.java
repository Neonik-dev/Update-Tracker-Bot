package ru.tinkoff.edu.java.scrapper.persistence.entity.domain;

import com.google.gson.Gson;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ConvertorStack implements AttributeConverter<StackOverFlowJson, String> {
    private final static Gson GSON = new Gson();

    @Override
    public String convertToDatabaseColumn(StackOverFlowJson gitHubJson) {
        return GSON.toJson(gitHubJson);
    }

    @Override
    public StackOverFlowJson convertToEntityAttribute(String dbData) {
        return GSON.fromJson(dbData, StackOverFlowJson.class);
    }
}
