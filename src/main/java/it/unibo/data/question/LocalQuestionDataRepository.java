package it.unibo.data.question;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import it.unibo.model.data.question.QuestionDTO;
import it.unibo.model.data.question.QuestionLoadingException;
import it.unibo.model.data.question.api.QuestionDataRepository;
import it.unibo.model.question.Difficulty;

/**
 * Loads questions from a local data source. 
 */
public final class LocalQuestionDataRepository implements QuestionDataRepository {

    private static final int QUESTIONS_PER_DIFFICULTY = 6;

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
    public List<QuestionDTO> loadQuestions()
            throws QuestionLoadingException {

        try (InputStream inputStream = LocalQuestionDataRepository.class.getResourceAsStream(
                this.questionFilePath
            )) {

            if (inputStream == null) {
                throw new QuestionLoadingException(
                    "File not found: " + this.questionFilePath
                );
            }

            final String json = new String(
                inputStream.readAllBytes(),
                StandardCharsets.UTF_8
            );

            if (json.isBlank()) {
                throw new QuestionLoadingException(
                    "The local source returned an empty content"
                );
            }

            final TriviaParser parser = new TriviaParser(this.mapper);
            final List<QuestionDTO> allDTOs =
                parser.parseTrivia(json);

            final List<QuestionDTO> balancedDTOs =
                new ArrayList<>();

            for (final Difficulty difficulty : Difficulty.values()) {

                final List<QuestionDTO> filtered =
                    new ArrayList<>(
                        allDTOs.stream()
                            .filter(question ->
                                question.difficulty() == difficulty
                            )
                            .toList()
                    );

                Collections.shuffle(filtered);

                if (filtered.size() < QUESTIONS_PER_DIFFICULTY) {
                    throw new QuestionLoadingException(
                        "Not enough questions for difficulty "
                            + difficulty
                    );
                }

                balancedDTOs.addAll(
                    filtered.stream()
                        .limit(QUESTIONS_PER_DIFFICULTY)
                        .toList()
                );
            }

    

            return List.copyOf(balancedDTOs);

        } catch (final IOException exception) {
            throw new QuestionLoadingException(
                "Error loading questions from local source: "
                    + exception.getMessage(),
                exception
            );
        }
    }
}
