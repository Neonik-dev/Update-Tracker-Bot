package ru.tinkoff.edu.java.scrapper.clients.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GitHubResponse (
        @JsonProperty("full_name") String fullName,
        @JsonProperty("created_at") OffsetDateTime createdAt,
        @JsonProperty("updated_at") OffsetDateTime updatedAt
) {
}
