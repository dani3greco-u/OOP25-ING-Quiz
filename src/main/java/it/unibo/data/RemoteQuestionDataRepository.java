package it.unibo.data;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import it.unibo.model.data.QuestionDTO;
import it.unibo.model.data.QuestionLoadingException;
import it.unibo.model.data.api.QuestionDataRepository;
import it.unibo.model.question.Difficulty;

/**
 * Loads questions from a remote data source.
 */
public final class RemoteQuestionDataRepository implements QuestionDataRepository {

    /**
     * The number of questions to load for each difficulty level.
     */
    private static final int QUESTIONS_PER_DIFFICULTY = 6;

    private static final int CONNECTION_TIMEOUT_SECONDS = 5;
    private static final int REQUEST_TIMEOUT_SECONDS = 10;

    private final String urlJson;
    private final ObjectMapper mapper;
    private final HttpClient client;

    /**
     * Creates a new instance of RemoteQuestionDataRepository.
     * 
     * @param urlJson the URL of the JSON endpoint containing the questions.
     */
    public RemoteQuestionDataRepository(final String urlJson) {
        this.urlJson = Objects.requireNonNull(
            urlJson,
            "The remote URL cannot be null"
        );

        if (this.urlJson.isBlank()) {
            throw new IllegalArgumentException(
                "The remote URL cannot be blank"
            );
        }
        this.mapper = JsonMapper.builder()
            .findAndAddModules()
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .build();
        this.client = HttpClient.newBuilder()
                        .connectTimeout(
                            Duration.ofSeconds(CONNECTION_TIMEOUT_SECONDS)
                        ).build();
    }

    /**
     * Creates a new instance of RemoteQuestionDataRepository with custom ObjectMapper and HttpClient.
     * 
     * @param urlJson the URL of the JSON endpoint containing the questions.
     * @param mapper the ObjectMapper to use for parsing the JSON response.
     * @param client the HttpClient to use for making the HTTP request.
     */
    public RemoteQuestionDataRepository(final String urlJson, final ObjectMapper mapper, final HttpClient client) {
        this.urlJson = Objects.requireNonNull(
            urlJson,
            "The remote URL cannot be null"
        );

        if (this.urlJson.isBlank()) {
            throw new IllegalArgumentException(
                "The remote URL cannot be blank"
            );
        }

        this.mapper = Objects.requireNonNull(
            mapper,
            "The ObjectMapper cannot be null"
        );

        this.client = Objects.requireNonNull(
            client,
            "The HttpClient cannot be null"
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<QuestionDTO> loadQuestions() throws QuestionLoadingException {
        try {
            final HttpRequest request = HttpRequest.newBuilder()
                                        .GET()
                                        .header("accept", "application/json")
                                        .timeout(
                                            Duration.ofSeconds(REQUEST_TIMEOUT_SECONDS)
                                        )
                                        .uri(URI.create(this.urlJson))
                                        .build();
            final HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != HttpURLConnection.HTTP_OK) {
                throw new QuestionLoadingException("Error loading questions from remote source: HTTP status code " 
                                                    + response.statusCode()
                );
            }
            if (response.body() == null || response.body().isBlank()) {
                throw new QuestionLoadingException(
                    "The remote source returned an empty response body"
                );
            }
            final TriviaParser parser = new TriviaParser(mapper);
            final List<QuestionDTO> allDTOs = parser.parseTrivia(response.body());

            final List<QuestionDTO> balanceDTOs = new ArrayList<>();
            for (final Difficulty diff : Difficulty.values()) {
                // 6 question for each difficulty
                final List<QuestionDTO> filtered = allDTOs.stream()
                    .filter(q -> q.difficulty() == diff)
                    .limit(QUESTIONS_PER_DIFFICULTY)
                    .toList();

                if (filtered.size() < QUESTIONS_PER_DIFFICULTY) {
                    throw new QuestionLoadingException("Not enough question for difficulty " + diff);
                }

                balanceDTOs.addAll(filtered);
            }

            return balanceDTOs;
        } catch (final IOException e) {
            throw new QuestionLoadingException(
                "Error loading questions from remote source: "
                    + e.getMessage(),
                e
            );
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();

            throw new QuestionLoadingException(
                "Remote question loading was interrupted",
                e
            );
        } catch (final IllegalArgumentException e) {
            throw new QuestionLoadingException(
                "Invalid remote URL: " + this.urlJson,
                e
            );
        }
    }
}
