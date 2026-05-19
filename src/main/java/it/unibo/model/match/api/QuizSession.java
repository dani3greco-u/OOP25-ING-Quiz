package it.unibo.model.match.api;

import it.unibo.data.QuestionLoadingException;

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

    
}
