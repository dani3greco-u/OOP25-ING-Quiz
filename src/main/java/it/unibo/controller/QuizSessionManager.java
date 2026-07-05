package it.unibo.controller;

import java.util.Objects;

import it.unibo.model.data.question.QuestionLoadingException;
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
    private String currentPlayerName;
    private GameMode currentGameMode;

    /**
     * Creates a new quiz session manager.
     *
     * @param sessionFactory the factory used to create quiz sessions
     */
    public QuizSessionManager(final QuizSessionFactory sessionFactory) {
        this.sessionFactory = Objects.requireNonNull(
            sessionFactory,
            "The quiz session factory cannot be null"
        );
    }

    /**
     * Creates and starts a new quiz session for the specified player.
     *
     * <p>
     * Any previously stored session is replaced.
     * </p>
     *
     * @param playerName the name of the current player
     * @param gameMode the selected game mode
     * @throws NullPointerException if playerName or gameMode is null
     * @throws IllegalArgumentException if playerName is blank
     * @throws QuestionLoadingException if the questions cannot be loaded
     */
    public void startNewSession(final String playerName, final GameMode gameMode) throws QuestionLoadingException {
        Objects.requireNonNull(
            playerName,
            "The player name cannot be null"
        );
        Objects.requireNonNull(
            gameMode,
            "The game mode cannot be null"
        );

        final String normalizedName = playerName.trim();

        if (normalizedName.isEmpty()) {
            throw new IllegalArgumentException("The player name cannot be blank");
        }

        final QuizSession newSession = this.sessionFactory.createSession();

        newSession.startNewGame();

        this.currentSession = newSession;
        this.currentPlayerName = normalizedName;
        this.currentGameMode = gameMode;
    }

    /**
     * Returns the current quiz session.
     *
     * @return the current quiz session
     * @throws IllegalStateException if no session is active
     */
    public QuizSession getCurrentSession() {
        this.ensureActiveSession();
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
     * Returns the name of the current player.
     *
     * @return the current player's name
     * @throws IllegalStateException if no session is active
     */
    public String getCurrentPlayerName() {
        this.ensureActiveSession();
        return this.currentPlayerName;
    }

    /**
     * Returns the current game mode.
     *
     * @return the current game mode
     * @throws IllegalStateException if no session is active
     */
    public GameMode getCurrentGameMode() {
        this.ensureActiveSession();
        return this.currentGameMode;
    }

    /**
     * Checks whether the current session is a training session.
     *
     * @return true if the current session is in training mode
     * @throws IllegalStateException if no session is active
     */
    public boolean isTrainingSession() {
        return this.getCurrentGameMode() == GameMode.TRAINING;
    }

    /**
     * Removes the current quiz session.
     */
    public void closeSession() {
        this.currentSession = null;
        this.currentPlayerName = null;
        this.currentGameMode = null;
    }

    /**
     * Ensures that a quiz session is currently active.
     *
     * @throws IllegalStateException if no session is active
     */
    private void ensureActiveSession() {
        if (!this.hasActiveSession()) {
            throw new IllegalStateException("No quiz session is currently active");
        }
    }

}
