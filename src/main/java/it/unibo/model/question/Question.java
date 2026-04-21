package it.unibo.model.question;

import java.util.List;
import java.util.Objects;

import it.unibo.model.answer.Answer;

/**
 * This class represents a question in the quiz game. It contains the question id, difficulty, 
 * the question text and a list of possible answers.
 */
public class Question {
    private final int EXPECTED_ANSWERS = 4;

    private final String id;
    private final String text;
    private final Difficulty difficulty;
    private final List<Answer> answers;
    
    public Question(
        final String id,
        final String text,
        final Difficulty difficulty,
        final List<Answer> answers
    ) {
        this.id = Objects.requireNonNull(id);
        this.text = Objects.requireNonNull(text);
        this.difficulty = Objects.requireNonNull(difficulty);
        this.answers = List.copyOf(Objects.requireNonNull(answers));

        if (this.id.isBlank()) {
            throw new IllegalArgumentException("Question id cannot be blank");
        }
        if (this.text.isBlank()) {
            throw new IllegalArgumentException("Question text cannot be blank");
        }
        if (this.answers.size() != EXPECTED_ANSWERS) {
            throw new IllegalArgumentException("A question must have exactly 4 answers");
        }
        // REPORT
        if (this.answers.stream().filter(Answer::isCorrect).count() != 1) {
            throw new IllegalArgumentException("A question must have exactly 1 correct answer");
        }
    }

    public String getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public List<Answer> getAnswers() {
        return this.answers;
    }
}
