package it.unibo.answer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import it.unibo.model.answer.Answer;

/**
 * Test class for the Answer class.
 */
public class AnswerTest {
    
    /**
     * Test that a valid answer is created successfully and its getters return the expected values.
     */
    @Test
    void createsValidAnswer() {
        final Answer answer = new Answer("Stack", true);
        assertEquals("Stack", answer.getAnswer());
        assertEquals(true, answer.isCorrect());
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
