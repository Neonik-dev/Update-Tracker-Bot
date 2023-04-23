package ru.tinkoff.edu.java.scrapper.persistence.repository.jpa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ConverterDataChanges implements AttributeConverter<Map<String, String>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public String convertToDatabaseColumn(Map<String, String> customerInfo) {
        String customerInfoJson = null;
        try {
            customerInfoJson = objectMapper.writeValueAsString(customerInfo);
        } catch (final JsonProcessingException e) {
            log.error("JSON writing error");
        }

        return customerInfoJson;
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String customerInfoJSON) {
        Map<String, String> customerInfo = null;
        try {
            customerInfo = objectMapper.readValue(
                    customerInfoJSON,
                    new TypeReference<HashMap<String, String>>() {}
            );
        } catch (final IOException e) {
            log.error("JSON writing error");
        }
        return customerInfo;
    }
}
