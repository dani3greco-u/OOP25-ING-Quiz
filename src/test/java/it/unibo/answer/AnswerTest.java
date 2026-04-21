package it.unibo.answer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import it.unibo.model.answer.Answer;

/**
 * Test class for the Answer class.
 */
final class AnswerTest {

    /**
     * Test that a valid answer is created successfully and its getters return the expected values.
     */
    @Test
    void createsValidAnswer() {
        final Answer answer = new Answer("Stack", true);
        assertEquals("Stack", answer.getText());
        assertTrue(answer.isCorrect());
    }

    /**
     * Test that the constructor throws the expected exceptions when invalid parameters are provided.
     */
    @Test
    void throwsWhenAnswerIsNull() {
        assertThrows(NullPointerException.class, 
            () -> new Answer(null, true));
    }

    /**
     * Test that the constructor throws the expected exceptions when invalid parameters are provided.
     */
    @Test
    void throwsWhenAnswerIsBlank() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Answer(" ", true));
    }
}
