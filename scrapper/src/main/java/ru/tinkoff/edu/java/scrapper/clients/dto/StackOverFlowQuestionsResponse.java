package ru.tinkoff.edu.java.scrapper.clients.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record StackOverFlowQuestionsResponse(
        @JsonProperty("items") StackOverFlowDetailsResponse[] questions
) {
}
