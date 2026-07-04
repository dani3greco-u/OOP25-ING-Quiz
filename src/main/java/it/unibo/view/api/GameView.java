package it.unibo.view.api;

import java.util.List;
import java.util.function.Consumer;

/**
 * Defines the operations available in the game screen.
 * 
 * <p>
 * The controller uses this interface to display questions,
 * update the game progress and receive the answer selected
 * by the user.
 * </p>
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
     * @param listener the answer listener
     */
    void setAnswerListener(Consumer<Integer> listener);

    /**
     * Registers the 50:50 help listener.
     *
     * @param listener the listener to execute
     */
    void setFiftyFiftyListener(Runnable listener);

    /**
     * Registers the Double Chance help listener.
     *
     * @param listener the listener to execute
     */
    void setDoubleChanceListener(Runnable listener);

    /**
     * Registers the Switch help listener.
     *
     * @param listener the listener to execute
     */
    void setSwitchListener(Runnable listener);

    /**
     * Disables one answer.
     *
     * @param answerIndex the index of the answer to disable
     */
    void disableAnswer(int answerIndex);

    /**
     * Disables the 50:50 help button.
     */
    void disableFiftyFifty();

    /**
     * Disables the Double Chance help button.
     */
    void disableDoubleChance();

    /**
     * Disables the Switch help button.
     */
    void disableSwitch();

    /**
     * Enables all help buttons.
     */
    void enableAllHelps();

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
     * @param correctAnswer the correct answer
     */
    void showGameOver(int finalScore, String correctAnswer);

    /**
     * Shows the victory message.
     *
     * @param finalScore the final score
     */
    void showGameWon(int finalScore);

    /**
     * Shows a message confirming that the 50:50 help was used.
     */
    void showFiftyFiftyUsed();

    /**
     * Shows a message confirming that Double Chance was activated.
     */
    void showDoubleChanceUsed();

    /**
     * Shows a message confirming that the current question was switched.
     */
    void showSwitchUsed();
}
