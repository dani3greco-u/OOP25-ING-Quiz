package it.unibo.model.question.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.model.answer.Answer;
import it.unibo.model.data.question.QuestionDTO;
import it.unibo.model.question.Difficulty;
import it.unibo.model.question.Question;

/**
 * Test class for QuestionFactory.
 */
final class QuestionFactoryTest {

    private QuestionFactory factory;
    private QuestionDTO dto;

    @BeforeEach
    void setUp() {
        this.factory = new QuestionFactory();
        this.dto = new QuestionDTO(
            "multiple",
            Difficulty.MEDIUM,
            "Math",
            "What is 2 + 2?",
            "4",
            List.of("1", "2", "3")
        );
    }

    @Test
    void fromDTOCreatedQuestionCorrectly() {
        final Question question = factory.fromDTO(dto);

        assertNotNull(question);
        assertEquals(Difficulty.MEDIUM, question.getDifficulty());
        assertEquals("What is 2 + 2?", question.getText());
        assertNotNull(question.getAnswers());
        assertEquals(4, question.getAnswers().size());
    }

    @Test
    void testFromDtoCreatesOneCorrectAnswer() {
        final Question question = factory.fromDTO(dto);

        final long correctAnswers = question.getAnswers().stream()
            .filter(Answer::isCorrect)
            .count();
        final long incorrectAnswers = question.getAnswers().stream()
            .filter(a -> !a.isCorrect())
            .count();

        assertEquals(1, correctAnswers);
        assertEquals(3, incorrectAnswers);
    }

    @Test
    void testFromDtoIncludesCorrectAndIncorrectAnswers() {
        final Question question = factory.fromDTO(dto);

        final List<String> answerTexts = question.getAnswers().stream()
            .map(Answer::getText)
            .toList();

        assertTrue(answerTexts.contains("4"));
        assertTrue(answerTexts.contains("1"));
        assertTrue(answerTexts.contains("2"));
        assertTrue(answerTexts.contains("3"));
    }
}
