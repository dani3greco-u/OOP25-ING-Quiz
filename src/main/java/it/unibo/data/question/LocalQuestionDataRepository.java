package it.unibo.data.question;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import it.unibo.model.data.question.QuestionDTO;
import it.unibo.model.data.question.QuestionLoadingException;
import it.unibo.model.data.question.api.QuestionDataRepository;

/**
 * Loads questions from a local data source. 
 */
public final class LocalQuestionDataRepository implements QuestionDataRepository {

    private final String questionFilePath;
    private final ObjectMapper mapper;

    /**
     * Creates a new instance of LocalQuestionDataRepository.
     * 
     * @param questionFilePath the path to the JSON file containing the questions, relative to the classpath.
     */
    public LocalQuestionDataRepository(final String questionFilePath) {
        this.questionFilePath = questionFilePath;
        this.mapper = JsonMapper.builder()
            .findAndAddModules()
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<QuestionDTO> loadQuestions() throws QuestionLoadingException {

        try (InputStream is = LocalQuestionDataRepository.class.getResourceAsStream(this.questionFilePath)) {
            if (is == null) {
                throw new QuestionLoadingException("File not found: " + this.questionFilePath);
            }
            final TriviaParser parser = new TriviaParser(mapper);
            return parser.parseTrivia(new String(is.readAllBytes(), StandardCharsets.UTF_8));
        } catch (final IOException e) {
            throw new QuestionLoadingException("Failed to load questions: " + e.getMessage(), e);
        }
    }
}
