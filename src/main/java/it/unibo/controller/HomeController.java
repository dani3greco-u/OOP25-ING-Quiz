package it.unibo.controller;

import java.util.Objects;
import java.util.logging.Logger;

import it.unibo.model.data.leaderboard.api.Leaderboard;
import it.unibo.model.data.question.QuestionLoadingException;
import it.unibo.view.api.HomeView;
import it.unibo.view.api.QuizView;

/**
 * Handles the interactions of the home screen.
 */
public final class HomeController {

    private static final Logger LOGGER = Logger.getLogger(HomeController.class.getName());

    private final QuizView quizView;
    private final HomeView homeView;
    private final QuizSessionManager sessionManager;
    private final GameController gameController;
    private final Leaderboard leaderboard;

    /**
     * Creates the home controller.
     *
     * @param quizView the main application view
     * @param homeView the home view
     * @param sessionManager the quiz session manager
     * @param gameController the game controller
     * @param leaderboard the leaderboard
     */
    public HomeController(
        final QuizView quizView,
        final HomeView homeView,
        final QuizSessionManager sessionManager,
        final GameController gameController,
        final Leaderboard leaderboard
    ) {
        this.quizView = Objects.requireNonNull(
            quizView,
            "The quiz view cannot be null"
        );

        this.homeView = Objects.requireNonNull(
            homeView,
            "The home view cannot be null"
        );

        this.sessionManager = Objects.requireNonNull(
            sessionManager,
            "The session manager cannot be null"
        );

        this.gameController = Objects.requireNonNull(
            gameController,
            "The game controller cannot be null"
        );

        this.leaderboard = Objects.requireNonNull(
            leaderboard,
            "The leaderboard cannot be null"
        );

        attachListeners();
    }

    /**
     * Attaches the home view listeners.
     */
    private void attachListeners() {
        this.homeView.setOnStart(this::startGame);
        this.homeView.setOnTraining(this::startTraining);

        this.homeView.setOnLeaderboard(() -> {
        LOGGER.info(
            "Opening leaderboard."
        );

        this.homeView.showLeaderboard(this.leaderboard.getEntries());
        });

        this.homeView.setOnExit(this.quizView::close);
    }

    /**
     * Starts a new quiz game.
     *
     * @param playerName the player name
     */
    private void startGame(final String playerName) {
        try {
            LOGGER.info(
                "Starting a new game for: "
                    + playerName
            );

            this.sessionManager.startNewSession(playerName, GameMode.NORMAL);
            this.gameController.prepareNewGame();
            this.quizView.showGame();

        } catch (final QuestionLoadingException exception) {
            LOGGER.warning(
                "Error while loading questions: "
                    + exception.getMessage()
            );

        } catch (final IllegalStateException exception) {
            LOGGER.warning(
                "Unable to start the game: "
                    + exception.getMessage()
            );
        }
    }

    /**
     * Starts a new training session.
     *
     * @param playerName the player name
     */
    private void startTraining(final String playerName) {
        try {
            LOGGER.info(
                "Starting training mode for: "
                    + playerName
            );

            this.sessionManager.startNewSession(playerName, GameMode.TRAINING
            );

            this.gameController.prepareNewGame();
            this.quizView.showGame();

        } catch (final QuestionLoadingException exception) {
            LOGGER.warning(
                "Error while loading questions: "
                    + exception.getMessage()
            );

        } catch (final IllegalStateException exception) {
            LOGGER.warning(
                "Unable to start training: "
                    + exception.getMessage()
            );
        }
    }
}
