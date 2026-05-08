package it.unibo.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TriviaDTO(
    @JsonProperty("response_code") int responseCode,
    List<QuestionDTO> results
) {}

