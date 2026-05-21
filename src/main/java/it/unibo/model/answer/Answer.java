package it.unibo.model.answer;

import java.util.Objects;

/**
 * Class representing an answer to a question, containing the answer text and its correctness.
 */
public class Answer {
    private final String text;
    private final boolean isCorrect;
    private final boolean enabled;

    /**
     * Constructor for the Answer class.
     * 
     * @param text the answer given by the player
     * @param isCorrect true if the answer is correct, false otherwise
     */
    public Answer(final String text, final boolean isCorrect) {
        this.text = Objects.requireNonNull(text);
        if (this.text.isBlank()) {
            throw new IllegalArgumentException("Answer text cannot be blank");
        }
        this.isCorrect = Objects.requireNonNull(isCorrect);
        this.enabled = true;
    }

    /**
     * Return the answer given by the player.
     * 
     * @return the answer given by the player
     */
    public String getText() {
        return this.text;
    }

    /**
     * Return the correctness of the answer.
     * 
     * @return true if the answer is correct, false otherwise
     */
    public boolean isCorrect() {
        return this.isCorrect;
    }

    /**
     * Return the active status of the answer.
     * 
     * @return true if the answer is active, false otherwise
     */
    public boolean isEnabled() {
        return this.enabled;
    }

}
