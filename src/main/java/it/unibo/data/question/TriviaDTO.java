package it.unibo.data.question;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.unibo.model.data.question.QuestionDTO;

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
        results = results == null
                ? List.of()
                : List.copyOf(results);
    }
}

