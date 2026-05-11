package it.unibo.data;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import it.unibo.data.api.QuestionDataRepository;

/**
 * Loads questions from a remote data source.
 */
public class RemoteQuestionDataRepository implements QuestionDataRepository {

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
     * @ineheritDoc
     */
    @Override
    public List<QuestionDTO> loadQuestions() throws QuestionLoadingException {
        HttpRequest request = HttpRequest.newBuilder()
                                .GET()
                                .header("accept", "application/json")
                                .uri(URI.create(urlJson))
                                .build();
        try {
            HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() != 200) {
                throw new QuestionLoadingException("Error loading questions from remote source: HTTP status code " + response.statusCode());
            }
            final TriviaParser parser = new TriviaParser(mapper);
            return parser.parseTrivia(response.body());
        } catch (final IOException e) {
            throw new QuestionLoadingException("Error loading questions from remote source: " + e.getMessage(), e);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new QuestionLoadingException("Error loading questions from remote source: " + e.getMessage(), e);
        }
    }
}
