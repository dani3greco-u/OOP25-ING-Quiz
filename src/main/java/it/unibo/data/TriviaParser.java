package it.unibo.data;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility class for parsing trivia questions from JSON data.
 */
public class TriviaParser {
    private final ObjectMapper mapper;
    
    public TriviaParser(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Parses trivia questions from a JSON string and returns a list of QuestionDTO objects.
     * 
     * @param json the JSON string containing the trivia questions
     * @return a list of QuestionDTO objects parsed from the JSON string
     * @throws QuestionLoadingException if there is an error during parsing
     */
    public List<QuestionDTO> parseTrivia(String json) throws QuestionLoadingException {
        try {
            final TriviaDTO trivia = this.mapper.readValue(json, TriviaDTO.class);
            return List.copyOf(trivia.results());
        } catch (IOException e) {
            throw new QuestionLoadingException("Error parsing trivia JSON: " + e.getMessage(), e);
        }
    }
}
