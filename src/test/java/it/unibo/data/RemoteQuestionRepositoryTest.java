package it.unibo.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.unibo.model.data.QuestionDTO;
import it.unibo.model.data.QuestionLoadingException;
import it.unibo.model.question.Difficulty;

import org.junit.jupiter.api.Test;

@ExtendWith(MockitoExtension.class)
class RemoteQuestionRepositoryTest {

    @Mock
    private HttpClient client;

    @Mock
    private HttpResponse<String> response;

    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    /**
     * Helper method to generate a mock JSON response with 18 questions (6 for each difficulty level)
     */
    private String generateMockJson() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{\"response_code\": 0, \"results\": [");
        
        final String template = "{\"type\":\"multiple\",\"difficulty\":\"%s\",\"category\":\"Test\",\"question\":\"Test Q\",\"correct_answer\":\"A\",\"incorrect_answers\":[\"B\",\"C\",\"D\"]}";
        
        // Generate 6 question for difficulty
        for (int i = 0; i < 6; i++) {
            sb.append(String.format(template, Difficulty.EASY)).append(",");
            sb.append(String.format(template, Difficulty.MEDIUM)).append(",");
        }
        for (int i = 0; i < 6; i++) {
            sb.append(String.format(template, Difficulty.HARD));
            if (i < 5) {
                sb.append(","); // add ',' but not in the last
            }
        }
        
        sb.append("]}");
        return sb.toString();
    }

    @Test
    void testLoadQuestionsSuccess() throws IOException, InterruptedException, QuestionLoadingException {
        final String json = generateMockJson();
        
        when(this.client.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
            .thenReturn(this.response);
        when(this.response.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(this.response.body()).thenReturn(json);

        final var repository = new RemoteQuestionDataRepository("http://localhost/test", mapper, client);

        final List<QuestionDTO> result = repository.loadQuestions();

        assertNotNull(result);
        assertEquals(18, result.size(), "The list of questions should contain exactly 18 questions");
        
        verify(this.client).send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any());
    }

    @Test
    void testLoadQuestionsThrowsIOException() throws IOException, InterruptedException {
        final var repository = new RemoteQuestionDataRepository("http://localhost/test", mapper, client);

        when(this.client.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenThrow(new IOException("Network error"));

        final QuestionLoadingException ex = assertThrows(
                QuestionLoadingException.class,
                repository::loadQuestions
        );

        assertTrue(ex.getMessage().contains("Error loading questions from remote source"));
    }

    @Test
    void testLoadQuestionNotEnough() throws IOException, InterruptedException {
        final String json = "{\"response_code\": 0, \"results\": []}";

        when(this.client.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(this.response);
        when(this.response.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(this.response.body()).thenReturn(json);
        final var repository = new RemoteQuestionDataRepository("http://localhost/notEnough", mapper, client);



        assertThrows(QuestionLoadingException.class, () -> repository.loadQuestions());

        verify(this.client).send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any());
    }
}
