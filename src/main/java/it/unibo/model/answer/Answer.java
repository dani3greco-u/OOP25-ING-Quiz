package it.unibo.model.answer;

import java.util.Objects;

public class Answer {
    private final String answer;
    private boolean isCorrect;

    /**
     * Constructor for the Answer class.
     * 
     * @param answer the answer given by the player
     * @param isCorrect true if the answer is correct, false otherwise
     */
    public Answer(String answer, boolean isCorrect) {
        this.answer = Objects.requireNonNull(answer);
        if (this.answer.isBlank()) {
            throw new IllegalArgumentException("Answer text cannot be blank");
        }
        this.isCorrect = Objects.requireNonNull(isCorrect);
    }

    /**
     * Return the answer given by the player.
     * 
     * @return the answer given by the player
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Return the correctness of the answer.
     * 
     * @return true if the answer is correct, false otherwise
     */
    public boolean isCorrect() {
        return isCorrect;
    }
}
