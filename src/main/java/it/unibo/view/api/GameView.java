package it.unibo.view.api;

import java.util.List;
import java.util.function.Consumer;

/**
 * Defines the operations available in the game screen.
 *
 * The controller uses this interface to display questions,
 * update the game progress and receive the answer selected
 * by the user.
 */
public interface GameView {

    /**
     * Sets the text of the current question.
     *
     * @param text the question text
     */
    void setQuestionText(String text);

    /**
     * Sets the texts of the available answers.
     *
     * @param answers the answer texts
     */
    void setAnswers(List<String> answers);

    /**
     * Updates the progress information shown to the user.
     *
     * @param currentQuestionNumber the current question number
     * @param totalQuestions the total number of questions
     * @param currentScore the current score
     */
    void updateProgress(
        int currentQuestionNumber,
        int totalQuestions,
        int currentScore
    );

    /**
     * Registers the listener invoked when an answer is selected.
     *
     * The listener receives the index of the selected answer.
     *
     * @param listener the answer listener
     */
    void setAnswerListener(Consumer<Integer> listener);

    /**
     * Disables one answer.
     *
     * @param answerIndex the index of the answer to disable
     */
    void disableAnswer(int answerIndex);

    /**
     * Shows a message indicating that the answer is correct.
     */
    void showCorrectAnswer();

    /**
     * Shows a message indicating that the answer is wrong.
     */
    void showWrongAnswer();

    /**
     * Shows the game-over message.
     *
     * @param finalScore the final score
     */
    void showGameOver(int finalScore);

    /**
     * Shows the victory message.
     *
     * @param finalScore the final score
     */
    void showGameWon(int finalScore);
}