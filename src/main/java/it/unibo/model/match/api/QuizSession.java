package it.unibo.model.match.api;

import java.util.List;

import it.unibo.data.QuestionLoadingException;
import it.unibo.model.answer.Answer;
import it.unibo.model.help.Switch;
import it.unibo.model.match.Match;
import it.unibo.model.question.Question;

/**
 * Represents a quiz session.
 */
public interface QuizSession {

    /**
     * Starts a new game session by loading questions from the repository and initializing the match state.
     * 
     * @throws QuestionLoadingException if there is an error loading questions from the repository
     */
    void startNewGame() throws QuestionLoadingException;

    /**
     * Returns the current question of the quiz session.
     * 
     * @return the current question of the quiz session
     */
    Question getCurrentQuestion();

    /**
     * Returns the current match instance.
     * 
     * @return the current match instance
     */
    Match getMatch();

    /**
     * Submits an answer for the current question and updates the match state accordingly.
     * 
     * @param answer the answer to submit for the current question
     * @throws IllegalStateException if the match is not in progress or if the answer cannot be submitted
     */
    void submitAnswer(Answer answer);

    /**
     * Uses the fifty-fifty lifeline to eliminate two incorrect answers for the current question.
     * 
     * @throws IllegalStateException if the fifty-fifty lifeline has already been used or if the match is not in progress
     */
    void useFiftyFifty();

    /**
     * Returns the list of answers that have been disabled by the fifty-fifty lifeline.
     * 
     * @return the list of disabled answers
     */
    List<Answer> getDisabledAnswers();

    /**
     * Uses the double chance help strategy to allow the player to have two attempts to answer a question correctly.
     * 
     * @throws IllegalStateException if the double chance help has already been used or if the match is not in progress
     */
    void useDoubleChance();

    /** 
     * Returns true if the double chance help strategy is currently active, false otherwise.
     * 
     * @return true if the double chance help strategy is currently active, false otherwise
     */
    boolean isDoubleChanceActive();

    /**
     * Uses the switch help strategy to allow the player to switch to a different question.
     * 
     * @throws IllegalStateException if the switch help has already been used or if the match is not in progress
     */
    void useSwitch();

    /**
     * Getter for the Switch help strategy.
     * 
     * @return the Switch help strategy
     */
    Switch getSwitchHelp();
}
