package it.unibo.data.question;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.unibo.model.data.question.QuestionDTO;
import it.unibo.model.data.question.QuestionLoadingException;

/**
 * Utility class for parsing trivia questions from JSON data.
 */
public class TriviaParser {
    private final ObjectMapper mapper;

    /**
     * Constructs a TriviaParser with the specified ObjectMapper.
     * 
     * @param mapper the ObjectMapper to use for parsing JSON data
     */
    public TriviaParser(final ObjectMapper mapper) {
        this.mapper = Objects.requireNonNull(
            mapper,
            "The ObjectMapper cannot be null"
        );
    }

    /**
     * Parses trivia questions from a JSON string and returns a list of QuestionDTO objects.
     * 
     * @param json the JSON string containing the trivia questions
     * @return a list of QuestionDTO objects parsed from the JSON string
     * @throws QuestionLoadingException if there is an error during parsing
     */
    public List<QuestionDTO> parseTrivia(final String json) throws QuestionLoadingException {
        Objects.requireNonNull(
            json,
            "The JSON string cannot be null"
        );
        try {
            final TriviaDTO trivia = this.mapper.readValue(json, TriviaDTO.class);

            if (trivia.responseCode() != 0) {
                throw new QuestionLoadingException(
                    "The trivia API returned response code "
                        + trivia.responseCode()
                );
            }

            if (trivia.results().isEmpty()) {
                throw new QuestionLoadingException(
                    "The trivia API returned no questions"
                );
            }

            return List.copyOf(trivia.results());
        } catch (final IOException e) {
            throw new QuestionLoadingException("Error parsing trivia JSON: " + e.getMessage(), e);
        }
    }
}
