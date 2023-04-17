package ru.tinkoff.edu.java.scrapper.clients.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GitHubSiteResponse(
        @JsonProperty("full_name") String fullName,
        @JsonProperty("created_at") OffsetDateTime createdAt,
        @JsonProperty("updated_at") OffsetDateTime updatedAt
) implements BaseSiteResponse {
    @Override
    public Map<String, String> getMap() {
        return Map.of("updated_date", updatedAt().toString(), "full_name", fullName(), "created_at", createdAt().toString());
    }
}
