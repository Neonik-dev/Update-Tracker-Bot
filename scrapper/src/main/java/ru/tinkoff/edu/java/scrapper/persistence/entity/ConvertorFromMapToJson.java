package ru.tinkoff.edu.java.scrapper.persistence.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import org.postgresql.util.PGobject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public final class ConvertorFromMapToJson implements AttributeConverter<Map<String, String>, PGobject> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public PGobject convertToDatabaseColumn(Map<String, String> map) {
        PGobject po = new PGobject();
        po.setType("jsonb");

        try {
            po.setValue(MAPPER.writeValueAsString(map));
        } catch (SQLException | JsonProcessingException ex) {
            throw new IllegalStateException(ex);
        }
        return po;
    }


    @Override
    public Map<String, String> convertToEntityAttribute(PGobject dbData) {
        if (dbData == null || dbData.getValue() == null) {
            return null;
        }
        try {
            return MAPPER.readValue(dbData.getValue(), new TypeReference<>() {
            });
        } catch (IOException ex) {
            return null;
        }
    }

}