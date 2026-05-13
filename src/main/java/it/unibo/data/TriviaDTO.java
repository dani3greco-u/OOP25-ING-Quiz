package it.unibo.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for the trivia API response.
 * 
 * @param responseCode the response code from the API
 * @param results the list of questions retrieved from the API
 */ 
public record TriviaDTO(
    @JsonProperty("response_code") int responseCode,
    List<QuestionDTO> results
) { 
    /**
     * Constructs a new TriviaDTO instance.
     * 
     * @param responseCode the response code from the API
     * @param results the list of questions retrieved from the API
     */
    public TriviaDTO {
        results = List.copyOf(results);
    }
}

