package it.unibo.data;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

@ExtendWith(MockitoExtension.class)
class RemoteQuestionRepositoryTest {

    @Mock
    private HttpClient client;

    @Mock
    private HttpResponse<String> response;

    private RemoteQuestionDataRepository repository;
    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void testLoadQuestionsSuccess() throws Exception {
        String json = "{\"response_code\": 0, \"results\": []}";
        when(this.client.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
            .thenReturn(this.response);
        when(this.response.statusCode()).thenReturn(200);
        when(this.response.body()).thenReturn(json);

        this.repository = new RemoteQuestionDataRepository("http://localhost/test", mapper, client);

        List<QuestionDTO> result = this.repository.loadQuestions();

        assertNotNull(result);
        verify(this.client).send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any());
    }

    @Test
    void testLoadQuestionsThrowsIOException() throws Exception {
        this.repository = new RemoteQuestionDataRepository("http://localhost/test", mapper, client);

        when(this.client.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenThrow(new IOException("Network error"));

        QuestionLoadingException ex = assertThrows(
                QuestionLoadingException.class,
                () -> this.repository.loadQuestions()
        );

        assertTrue(ex.getMessage().contains("Error loading questions from remote source"));
    }
}
