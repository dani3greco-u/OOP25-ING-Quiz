package it.unibo.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.model.question.Difficulty;

final class LocalQuestionRepositoryTest {

    private LocalQuestionDataRepository repository;

    @BeforeEach
    void setUp() {
        final String question_file_path = "/test.json";
        this.repository = new LocalQuestionDataRepository(question_file_path);
    }

    @Test
    void testLoadQuestionsNotNull() throws QuestionLoadingException {
        List<QuestionDTO> questions = repository.loadQuestions();
        assertNotNull(questions);
    }

    @Test
    void testLoadQuestionsNotEmpty() throws QuestionLoadingException {
        List<QuestionDTO> questions = repository.loadQuestions();
        assertNotNull(questions);
        assertTrue(questions.size() > 0, "The list of questions should not be empty");
        assertEquals(10, questions.size(), "The list of questions should contain exactly 10 question");
    }

    @Test
    void testLoadQuestionsContent() throws QuestionLoadingException {
    List<QuestionDTO> questions = repository.loadQuestions();
    assertNotNull(questions, "The list of questions should not be null");
    assertEquals(10, questions.size(), "The list of questions should contain exactly 10 question");
    QuestionDTO first = questions.get(0);
    assertEquals("multiple", first.type());
    assertEquals(Difficulty.MEDIUM, first.difficulty());
    assertEquals("Science: Computers", first.category());
    assertTrue(first.question().contains("AD stand for"));
    assertEquals("Active Directory", first.correctAnswer());
    assertNotNull(first.incorrectAnswers());
    assertEquals(3, first.incorrectAnswers().size());
    assertTrue(first.incorrectAnswers().contains("Alternative Drive"));
}

    @Test
    void testLoadQuestionsFromEmptyFile() {
        final String question_file_path = "/empty.json";
        var emptyRepo = new LocalQuestionDataRepository(question_file_path);
        assertThrows(QuestionLoadingException.class, () -> {
            emptyRepo.loadQuestions();
        });
    }

    @Test
    void testLoadMalformedJson() {
        final String path = "/corrupt.json";
        var corruptRepo = new LocalQuestionDataRepository(path);
        
        assertThrows(QuestionLoadingException.class, () -> {
            corruptRepo.loadQuestions();
        });
    }

    @Test
    void testIncorrectAnswersSize() throws QuestionLoadingException {
        List<QuestionDTO> questions = repository.loadQuestions();
        assertNotNull(questions, "The list of questions should not be null");
        assertEquals(10, questions.size(), "The list of questions should contain exactly 10 question");
        assertEquals(3, questions.get(0).incorrectAnswers().size());
        assertEquals(1, questions.get(4).incorrectAnswers().size());
    }

    @Test
    void testLoadQuestionsIsUnmodifiable() throws QuestionLoadingException {
        List<QuestionDTO> questions = repository.loadQuestions();
        assertNotNull(questions, "The list of questions should not be null");
        assertEquals(10, questions.size(), "The list of questions should contain exactly 10 question");

        QuestionDTO question = new QuestionDTO(
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
