package it.unibo.controller;

import java.util.Objects;
import java.util.logging.Logger;

import it.unibo.model.data.QuestionLoadingException;
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

    /**
     * Creates the home controller.
     *
     * @param quizView the main application view
     * @param homeView the home view
     * @param sessionManager the quiz session manager
     * @param gameController the game controller
     */
    public HomeController(
        final QuizView quizView,
        final HomeView homeView,
        final QuizSessionManager sessionManager,
        final GameController gameController
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

        attachListeners();
    }

    /**
     * Attaches the home view listeners.
     */
    private void attachListeners() {
        this.homeView.setOnStart(this::startGame);

        this.homeView.setOnTraining(playerName -> {
            LOGGER.info(
                "[HOME CONTROLLER] Training mode requested by: "
                    + playerName
            );
        });

        this.homeView.setOnLeaderboard(() -> {
            LOGGER.info(
                "[HOME CONTROLLER] Opening leaderboard."
            );
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
                "[HOME CONTROLLER] Starting a new game for: "
                    + playerName
            );

            this.sessionManager.startNewSession();
            this.gameController.prepareNewGame();
            this.quizView.showGame();

        } catch (final QuestionLoadingException exception) {
            LOGGER.warning(
                "[HOME CONTROLLER] Error while loading questions: "
                    + exception.getMessage()
            );

        } catch (final IllegalStateException exception) {
            LOGGER.warning(
                "[HOME CONTROLLER] Unable to start the game: "
                    + exception.getMessage()
            );
        }
    }
}
