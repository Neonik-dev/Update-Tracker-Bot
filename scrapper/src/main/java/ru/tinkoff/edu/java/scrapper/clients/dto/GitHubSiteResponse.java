package ru.tinkoff.edu.java.scrapper.clients.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GitHubSiteResponse(
        @JsonProperty("id") Long id,
        @JsonProperty("pushed_at") OffsetDateTime updatedDate
) implements BaseSiteResponse {
}
