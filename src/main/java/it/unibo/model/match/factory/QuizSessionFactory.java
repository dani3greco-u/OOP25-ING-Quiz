package it.unibo.model.match.factory;

import it.unibo.model.match.api.QuizSession;

/**
 * Factory responsible for creating new quiz sessions.
 */
public interface QuizSessionFactory {

    /**
     * Creates a new quiz session.
     *
     * @return a new quiz session
     */
    QuizSession createSession();
}