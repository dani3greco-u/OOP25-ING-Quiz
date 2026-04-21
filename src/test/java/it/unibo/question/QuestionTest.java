package it.unibo.question;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import it.unibo.model.answer.Answer;
import it.unibo.model.question.Difficulty;
import it.unibo.model.question.Question;

/**
 * Test class for the Question class.
 */
final class QuestionTest {
    private static final List<Answer> VALID_ANSWERS = List.of(
        new Answer("Stack", false),
        new Answer("Queue", true),
        new Answer("Tree", false),
        new Answer("Graph", false)
    );

    /**
     * Test that a valid question is created successfully and its getters return the expected values.
     */
    @Test
    void createsValidQuestion() {
        final Question question = new Question(
            "q-1",
            "What structured is FIFO?",
            Difficulty.EASY,
            VALID_ANSWERS
        );

        assertEquals("q-1", question.getId());
        assertEquals("What structured is FIFO?", question.getText());
        assertEquals(Difficulty.EASY, question.getDifficulty());
        assertEquals(4, question.getAnswers().size());
    }

    /**
     * Test that the constructor throws the expected exceptions when invalid parameters are provided.
     */
    @Test
    void throwsWhenIdIsNull() {
        assertThrows(
            NullPointerException.class,
            () -> new Question(null, "Text", Difficulty.EASY, VALID_ANSWERS)
        );
    }

    /**
     * Test that the constructor throws the expected exceptions when invalid parameters are provided.
     */
    @Test
    void throwsWhenIdIsBlank() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Question("   ", "Text", Difficulty.EASY, VALID_ANSWERS)
        );
    }

    /**
     * Test that the constructor throws the expected exceptions when invalid parameters are provided.
     */
    @Test
    void throwsWhenTextIsNull() {
        assertThrows(
            NullPointerException.class,
            () -> new Question("q-1", null, Difficulty.EASY, VALID_ANSWERS)
        );
    }

    
    /**
     * Test that the constructor throws the expected exceptions when invalid parameters are provided.
     */
    @Test
    void throwsWhenTextIsBlank() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Question("q-1", "   ", Difficulty.EASY, VALID_ANSWERS)
        );
    }

    
    /**
     * Test that the constructor throws the expected exceptions when invalid parameters are provided.
     */
    @Test
    void throwsWhenDifficultyIsNull() {
        assertThrows(
            NullPointerException.class,
            () -> new Question("q-1", "Testo", null, VALID_ANSWERS)
        );
    }

    
    /**
     * Test that the constructor throws the expected exceptions when invalid parameters are provided.
     */
    @Test
    void throwsWhenAnswersAreNull() {
        assertThrows(
            NullPointerException.class,
            () -> new Question("q-1", "Testo", Difficulty.EASY, null)
        );
    }

    /**
     * Test that the constructor throws the expected exceptions when invalid parameters are provided.
     */
    @Test
    void throwsWhenAnswersAreLessThanFour() {
        final List<Answer> answers = List.of(
            new Answer("A", true),
            new Answer("B", false),
            new Answer("C", false)
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> new Question("q-1", "Testo", Difficulty.EASY, answers)
        );
    }
    
    /**
     * Test that the constructor throws the expected exceptions when invalid parameters are provided.
     */
    @Test
    void throwsWhenAnswersAreMoreThanFour() {
        final List<Answer> answers = List.of(
            new Answer("A", true),
            new Answer("B", false),
            new Answer("C", false),
            new Answer("D", false),
            new Answer("E", false)
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> new Question("q-1", "Testo", Difficulty.EASY, answers)
        );
    }
    
    /**
     * Test that the constructor throws the expected exceptions when invalid parameters are provided.
     */
    @Test
    void throwsWhenNoAnswerIsCorrect() {
        final List<Answer> answers = List.of(
            new Answer("A", false),
            new Answer("B", false),
            new Answer("C", false),
            new Answer("D", false)
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> new Question("q-1", "Text", Difficulty.EASY, answers)
        );
    }
    
    /**
     * Test that the constructor throws the expected exceptions when invalid parameters are provided.
     */
    @Test
    void throwsWhenMoreThanOneAnswerIsCorrect() {
        final List<Answer> answers = List.of(
            new Answer("A", true),
            new Answer("B", true),
            new Answer("C", false),
            new Answer("D", false)
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> new Question("q-1", "Testo", Difficulty.EASY, answers)
        );
    }
}
