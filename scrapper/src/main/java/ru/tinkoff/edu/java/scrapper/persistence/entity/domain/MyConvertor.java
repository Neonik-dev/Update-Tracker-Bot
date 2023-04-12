package ru.tinkoff.edu.java.scrapper.persistence.entity.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.postgresql.util.PGobject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@Converter(autoApply = true)
public final class MyConvertor implements AttributeConverter<Map<String, String>, PGobject> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public PGobject convertToDatabaseColumn(Map<String, String> map) {
        System.out.println(map.values());
        PGobject po = new PGobject();
        po.setType("jsonb");

        try {
            po.setValue(map == null ? null : MAPPER.writeValueAsString(map));
        } catch (SQLException | JsonProcessingException ex) {
            throw new IllegalStateException(ex);
        }
        System.out.println("gjjg" + po);
        return po;
    }


    @Override
    public Map<String, String> convertToEntityAttribute(PGobject dbData) {
        System.out.println("Тестируем конвертор");

        if (dbData == null || dbData.getValue() == null) {
            return null;
        }
        try {
            return MAPPER.readValue(dbData.getValue(), new TypeReference<Map<String, String>>() {
            });
        } catch (IOException ex) {
            return null;
        }
    }

}