package ru.tinkoff.edu.java.scrapper.clients.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record StackOverFlowSiteResponse(
        @JsonProperty("items") StackOverFlowDetailsResponse[] questions
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record StackOverFlowDetailsResponse(
            @JsonProperty("creation_date") OffsetDateTime createAt,
            @JsonProperty("last_edit_date") OffsetDateTime updatedDate,
            @JsonProperty("question_id") long id
    ) implements BaseSiteResponse {
    }
}
