package it.unibo.controller;

import java.util.Objects;

import it.unibo.data.leaderboard.JsonLeaderboardRepository;
import it.unibo.model.data.leaderboard.LeaderboardImpl;
import it.unibo.model.data.leaderboard.api.Leaderboard;
import it.unibo.model.match.factory.QuizSessionFactory;
import it.unibo.view.api.GameView;
import it.unibo.view.api.HomeView;
import it.unibo.view.api.QuizView;

/**
 * Coordinates the controllers of the application.
 */
public final class QuizController {

    private final QuizSessionManager sessionManager;
    private final GameController gameController;

    /**
     * Creates and connects the application controllers.
     *
     * @param quizView the main application view
     * @param sessionFactory the factory used to create quiz sessions
     */
    public QuizController(
        final QuizView quizView,
        final QuizSessionFactory sessionFactory
    ) {
        final QuizView checkedQuizView = Objects.requireNonNull(
            quizView,
            "The quiz view cannot be null"
        );

        final QuizSessionFactory checkedSessionFactory =
            Objects.requireNonNull(
                sessionFactory,
                "The quiz session factory cannot be null"
            );

        final HomeView homeView = Objects.requireNonNull(
            checkedQuizView.getHomeView(),
            "The home view cannot be null"
        );

        final GameView gameView = Objects.requireNonNull(
            checkedQuizView.getGameView(),
            "The game view cannot be null"
        );

        this.sessionManager = new QuizSessionManager(checkedSessionFactory);

        final Leaderboard leaderboard = new LeaderboardImpl(new JsonLeaderboardRepository());

        this.gameController = new GameController(
            checkedQuizView,
            gameView,
            this.sessionManager,
            leaderboard
        );

        new HomeController(
            checkedQuizView,
            homeView,
            this.sessionManager,
            this.gameController,
            leaderboard
        );
    }
}
