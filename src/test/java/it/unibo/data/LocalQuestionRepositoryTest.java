package it.unibo.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.model.data.QuestionDTO;
import it.unibo.model.data.QuestionLoadingException;
import it.unibo.model.question.Difficulty;

final class LocalQuestionRepositoryTest {

    private LocalQuestionDataRepository repository;

    @BeforeEach
    void setUp() {
        final String questionFilePath = "/test.json";
        this.repository = new LocalQuestionDataRepository(questionFilePath);
    }

    @Test
    void testLoadQuestionsNotNull() throws QuestionLoadingException {
        final List<QuestionDTO> questions = repository.loadQuestions();
        assertNotNull(questions);
    }

    @Test
    void testLoadQuestionsNotEmpty() throws QuestionLoadingException {
        final List<QuestionDTO> questions = repository.loadQuestions();
        assertNotNull(questions);
        assertFalse(questions.isEmpty(), "The list of questions should not be empty");
        assertEquals(10, questions.size(), "The list of questions should contain exactly 10 question");
    }

    @Test
    void testLoadQuestionsFromEmptyFile() {
        final String questionFilePath = "/empty.json";
        final var emptyRepo = new LocalQuestionDataRepository(questionFilePath);
        assertThrows(QuestionLoadingException.class, () -> {
            emptyRepo.loadQuestions();
        });
    }

    @Test
    void testLoadMalformedJson() {
        final String questionFilePath = "/corrupt.json";
        final var corruptRepo = new LocalQuestionDataRepository(questionFilePath);
        assertThrows(QuestionLoadingException.class, () -> {
            corruptRepo.loadQuestions();
        });
    }

    @Test
    void testLoadQuestionsIsUnmodifiable() throws QuestionLoadingException {
        final List<QuestionDTO> questions = repository.loadQuestions();
        assertNotNull(questions, "The list of questions should not be null");
        assertEquals(10, questions.size(), "The list of questions should contain exactly 10 question");

        final QuestionDTO question = new QuestionDTO(
            "multiple",
            Difficulty.EASY,
            "Category",
            "Question?",
            "Correct",
            List.of("Wrong")
        );
        assertThrows(UnsupportedOperationException.class, () -> {
                    questions.add(question);
        }, "The list of questions should be unmodifiable");
    }
}

