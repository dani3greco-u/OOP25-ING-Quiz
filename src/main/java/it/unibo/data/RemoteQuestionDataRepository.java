package it.unibo.data;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import it.unibo.model.data.api.QuestionDataRepository;
import it.unibo.model.question.Difficulty;

/**
 * Loads questions from a remote data source.
 */
public class RemoteQuestionDataRepository implements QuestionDataRepository {

    /**
     * The number of questions to load for each difficulty level.
     */
    private static final int QUESTIONS_PER_DIFFICULTY = 6;

    private final String urlJson;
    private final ObjectMapper mapper;
    private final HttpClient client;

    /**
     * Creates a new instance of RemoteQuestionDataRepository.
     * 
     * @param urlJson the URL of the JSON endpoint containing the questions.
     */
    public RemoteQuestionDataRepository(final String urlJson) {
        this.urlJson = urlJson;
        this.mapper = JsonMapper.builder()
        .findAndAddModules()
        .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .build();
        this.client = HttpClient.newHttpClient();
    }

    /**
     * Creates a new instance of RemoteQuestionDataRepository with custom ObjectMapper and HttpClient.
     * 
     * @param urlJson the URL of the JSON endpoint containing the questions.
     * @param mapper the ObjectMapper to use for parsing the JSON response.
     * @param client the HttpClient to use for making the HTTP request.
     */
    public RemoteQuestionDataRepository(final String urlJson, final ObjectMapper mapper, final HttpClient client) {
        this.urlJson = urlJson;
        this.mapper = mapper.findAndRegisterModules();
        this.client = client;
    }

    /**
     * @ineheritDoc
     */
    @Override
    public List<QuestionDTO> loadQuestions() throws QuestionLoadingException {
        final HttpRequest request = HttpRequest.newBuilder()
                                .GET()
                                .header("accept", "application/json")
                                .uri(URI.create(urlJson))
                                .build();
        try {
            final HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != HttpURLConnection.HTTP_OK) {
                throw new QuestionLoadingException("Error loading questions from remote source: HTTP status code " 
                                                    + response.statusCode()
                );
            }
            final TriviaParser parser = new TriviaParser(mapper);
            final List<QuestionDTO> allDTOs= parser.parseTrivia(response.body());

            final List<QuestionDTO> balanceDTOs = new ArrayList<>();
            for (final Difficulty diff : Difficulty.values()) {
                // 6 question for each difficulty
                final List<QuestionDTO> filtered = allDTOs.stream()
                    .filter(q -> q.difficulty() == diff)
                    .limit(QUESTIONS_PER_DIFFICULTY)
                    .toList();

                if(filtered.size() < QUESTIONS_PER_DIFFICULTY) {
                    throw new QuestionLoadingException("Not enough question for difficulty " + diff);
                }

                balanceDTOs.addAll(filtered);
            }

            return balanceDTOs;
        } catch (final IOException e) {
            throw new QuestionLoadingException("Error loading questions from remote source: " + e.getMessage(), e);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new QuestionLoadingException("Error loading questions from remote source: " + e.getMessage(), e);
        }
    }
}
