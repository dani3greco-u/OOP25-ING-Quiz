package it.unibo.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.model.data.QuestionDTO;
import it.unibo.model.data.QuestionLoadingException;
import it.unibo.model.data.api.QuestionDataRepository;
import it.unibo.model.question.Difficulty;

final class FallbackQuestionRepositoryTest {

    private QuestionDataRepository primaryMock;
    private QuestionDataRepository fallbackMock;
    private FallbackQuestionDataRepository fallbackRepository;

    private List<QuestionDTO> createDummyQuestions(final int amount) {
        final List<QuestionDTO> list = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            // difficulty is not important in these tests
            list.add(new QuestionDTO("multiple", Difficulty.EASY, "Test", "Question " + i, "A", List.of("B", "C", "D")));
        }
        return list;
    }

    @BeforeEach
    void setUp() {
        // Creiamo dei finti repository usando Mockito
        this.primaryMock = mock(QuestionDataRepository.class);
        this.fallbackMock = mock(QuestionDataRepository.class);
        
        // Istanziamo il nostro Fallback passandogli i due mock
        this.fallbackRepository = new FallbackQuestionDataRepository(this.primaryMock, this.fallbackMock);
    }

    @Test
    void testPrimarySuccess() throws QuestionLoadingException {
        final List<QuestionDTO> dummyQuestions = createDummyQuestions(18);
        // configure primary to return a valid list of questions
        when(this.primaryMock.loadQuestions()).thenReturn(dummyQuestions);

        final List<QuestionDTO> result = this.fallbackRepository.loadQuestions();

        // in each mode we expect exactly 18 questions
        assertEquals(18, result.size());
        
        // verify that primary was called once
        verify(this.primaryMock, times(1)).loadQuestions();
        
        // if primary works, fallback should not be used
        verifyNoInteractions(this.fallbackMock);
    }

    @Test
    void testPrimaryFailsWithNotEnoughQuestionsFallbackSuccess() throws QuestionLoadingException {
        // primary fail
        when(this.primaryMock.loadQuestions())
            .thenThrow(new QuestionLoadingException("Not enough questions loaded from remote source"));
        
        final List<QuestionDTO> localQuestions = createDummyQuestions(18);
        // fallback works fine
        when(this.fallbackMock.loadQuestions()).thenReturn(localQuestions);

        final List<QuestionDTO> result = this.fallbackRepository.loadQuestions();

        // we expect exactly 18 questions, because the fallback should have worked
        assertNotNull(result);
        assertEquals(18, result.size());
        
        // verify that primary was called once
        verify(this.primaryMock, times(1)).loadQuestions();
        // if primary fails, fallback should be used
        verify(this.fallbackMock, times(1)).loadQuestions();
    }
}
