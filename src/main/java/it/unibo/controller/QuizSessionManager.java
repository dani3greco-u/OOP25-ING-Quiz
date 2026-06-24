package it.unibo.controller;

import java.util.Objects;

import it.unibo.model.data.QuestionLoadingException;
import it.unibo.model.match.api.QuizSession;
import it.unibo.model.match.factory.QuizSessionFactory;

/**
 * Manages the current quiz session.
 *
 * <p>
 * This class creates, stores and closes the active quiz session.
 * It does not contain game logic.
 * </p>
 */
public final class QuizSessionManager {

    private final QuizSessionFactory sessionFactory;

    private QuizSession currentSession;

    /**
     * Creates a new quiz session manager.
     *
     * @param sessionFactory the factory used to create quiz sessions
     */
    public QuizSessionManager( final QuizSessionFactory sessionFactory) {
        this.sessionFactory = Objects.requireNonNull(
            sessionFactory,
            "The quiz session factory cannot be null"
        );
    }

    /**
     * Creates and starts a new quiz session.
     *
     * <p>
     * Any previously stored session is replaced.
     * </p>
     *
     * @throws QuestionLoadingException if the questions cannot be loaded
     */
    public void startNewSession() throws QuestionLoadingException {
        final QuizSession newSession =
            this.sessionFactory.createSession();

        newSession.startNewGame();

        this.currentSession = newSession;
    }

    /**
     * Returns the current quiz session.
     *
     * @return the current quiz session
     * @throws IllegalStateException if no session is active
     */
    public QuizSession getCurrentSession() {
        if (this.currentSession == null) {
            throw new IllegalStateException(
                "No quiz session is currently active"
            );
        }

        return this.currentSession;
    }

    /**
     * Checks whether a quiz session is currently active.
     *
     * @return true if a session is active, false otherwise
     */
    public boolean hasActiveSession() {
        return this.currentSession != null;
    }

    /**
     * Removes the current quiz session.
     */
    public void closeSession() {
        this.currentSession = null;
    }
}