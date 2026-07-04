package it.unibo.controller;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import it.unibo.model.answer.Answer;
import it.unibo.model.data.leaderboard.api.Leaderboard;
import it.unibo.model.match.MatchState;
import it.unibo.model.match.api.QuizSession;
import it.unibo.model.question.Question;
import it.unibo.view.api.GameView;
import it.unibo.view.api.QuizView;

/**
 * Handles the game screen interactions and coordinates
 * the active quiz session with the game view.
 */
public final class GameController {

    private static final int TOTAL_QUESTIONS = 15;
    private static final Logger LOGGER = Logger.getLogger(GameController.class.getName());

    private final QuizView quizView;
    private final GameView gameView;
    private final QuizSessionManager sessionManager;
    private final Leaderboard leaderboard;

    /**
     * Creates the game controller.
     *
     * @param quizView the main application view
     * @param gameView the game view
     * @param sessionManager the quiz session manager
     * @param leaderboard the leaderboard 
     */
    public GameController(
        final QuizView quizView,
        final GameView gameView,
        final QuizSessionManager sessionManager,
        final Leaderboard leaderboard
    ) {
        this.quizView = Objects.requireNonNull(
            quizView,
            "The quiz view cannot be null"
        );

        this.gameView = Objects.requireNonNull(
            gameView,
            "The game view cannot be null"
        );

        this.sessionManager = Objects.requireNonNull(
            sessionManager,
            "The session manager cannot be null"
        );

        this.leaderboard = Objects.requireNonNull(
            leaderboard,
            "The leaderboard cannot be null"
        );

        attachListeners();
    }

    /**
     * Attaches the game view listeners.
     */
    private void attachListeners() {
        this.gameView.setAnswerListener(this::handleAnswer);
        this.gameView.setFiftyFiftyListener(this::handleFiftyFifty);
        this.gameView.setDoubleChanceListener(this::handleDoubleChance);
        this.gameView.setSwitchListener(this::handleSwitch);
    }

    /**
     * Prepares and displays the current question.
     */
    public void showCurrentQuestion() {
        final QuizSession session =
            this.sessionManager.getCurrentSession();

        final Question currentQuestion =
            session.getCurrentQuestion();

        final List<String> answerTexts =
            currentQuestion.getAnswers()
                .stream()
                .map(Answer::getText)
                .toList();

        this.gameView.setQuestionText(
            currentQuestion.getText()
        );

        this.gameView.setAnswers(answerTexts);

        final int currentQuestionNumber =
            session.getMatch().getQuestionNumber() + 1;

        this.gameView.updateProgress(
            currentQuestionNumber,
            TOTAL_QUESTIONS,
            session.getMatch().getScore()
        );
    }

    /**
     * Resets the game view for a new session.
     */
    public void prepareNewGame() {
        this.gameView.enableAllHelps();
        showCurrentQuestion();
    }

    /**
     * Handles the selected answer.
     *
     * @param answerIndex the selected answer index
     */
    private void handleAnswer(final int answerIndex) {
        if (!this.sessionManager.hasActiveSession()) {
            return;
        }

        try {
            final QuizSession session =
                this.sessionManager.getCurrentSession();

            final Question currentQuestion =
                session.getCurrentQuestion();

            final List<Answer> answers =
                currentQuestion.getAnswers();

            validateAnswerIndex(
                answerIndex,
                answers.size()
            );

            final Answer selectedAnswer =
                answers.get(answerIndex);

            final boolean correct =
                selectedAnswer.isCorrect();

            final boolean doubleChanceWasActive =
                session.isDoubleChanceActive();

            final String correctAnswer = session.getCurrentQuestion()
            .getAnswers()
            .stream()
            .filter(Answer::isCorrect)
            .map(Answer::getText)
            .findFirst()
            .orElse("Unknown");

            session.submitAnswer(selectedAnswer);
            if (correct) {
                handleCorrectAnswer();
            } else if (session.getMatch().getState() == MatchState.LOSE) {
                handleGameOver(correctAnswer);
            } else {
                handleWrongAnswer(correctAnswer, answerIndex, doubleChanceWasActive);
            }

        } catch (final IllegalStateException exception) {
            LOGGER.warning(
                "Unable to submit answer: "
                    + exception.getMessage()
            );
        }
    }

    /**
     * Handles the 50:50 help.
     */
    private void handleFiftyFifty() {
        if (!this.sessionManager.hasActiveSession()) {
            return;
        }

        try {
            final QuizSession session =
                this.sessionManager.getCurrentSession();

            session.useFiftyFifty();

            final List<Answer> answers =
                session.getCurrentQuestion().getAnswers();

            for (final Answer disabledAnswer : session.getDisabledAnswers()) {
                final int answerIndex = answers.indexOf(disabledAnswer);
                if (answerIndex >= 0) {
                    this.gameView.disableAnswer(answerIndex);
                }
            }

            this.gameView.disableFiftyFifty();
            this.gameView.showFiftyFiftyUsed();

        } catch (final IllegalStateException exception) {
            LOGGER.warning(
                "Unable to use 50:50: "
                    + exception.getMessage()
            );
        }
    }

    /**
     * Handles the Double Chance help.
     */
    private void handleDoubleChance() {
        if (!this.sessionManager.hasActiveSession()) {
            return;
        }

        try {
            final QuizSession session =
                this.sessionManager.getCurrentSession();

            session.useDoubleChance();
            this.gameView.disableDoubleChance();
            this.gameView.showDoubleChanceUsed();

        } catch (final IllegalStateException exception) {
            LOGGER.warning(
                "Unable to use Double Chance: "
                    + exception.getMessage()
            );
        }
    }

    /**
     * Handles the Switch help.
     */
    private void handleSwitch() {
        if (!this.sessionManager.hasActiveSession()) {
            return;
        }

        try {
            final QuizSession session = this.sessionManager.getCurrentSession();

            session.useSwitch();
            showCurrentQuestion();
            this.gameView.disableSwitch();
            this.gameView.showSwitchUsed();

        } catch (final IllegalStateException exception) {
            LOGGER.warning(
                "Unable to use Switch: "
                    + exception.getMessage()
            );
        }
    }

    /**
     * Handles a correct answer.
     */
    private void handleCorrectAnswer() {
        final QuizSession session =
            this.sessionManager.getCurrentSession();

        this.gameView.showCorrectAnswer();

        if (
            session.getMatch().getState()
                == MatchState.IN_PROGRESS
        ) {
            showCurrentQuestion();
        } else {
            handleGameWon();
        }
    }

    /**
     * Handles a wrong answer.
     *
     * @param answerIndex the wrong answer index
     * @param doubleChanceWasActive whether Double Chance was active
     */
    private void handleWrongAnswer(
        final String correctAnswer,
        final int answerIndex,
        final boolean doubleChanceWasActive
    ) {
        final QuizSession session =
            this.sessionManager.getCurrentSession();

        if (
            doubleChanceWasActive
            && session.getMatch().getState()
                == MatchState.IN_PROGRESS
        ) {
            this.gameView.showWrongAnswer();
            this.gameView.disableAnswer(answerIndex);
            return;
        }

        handleGameOver(correctAnswer);
    }

    /**
     * Handles a lost game.
     */
    private void handleGameOver(final String correctAnswer) {
        final QuizSession session = this.sessionManager.getCurrentSession();
        final int finalScore = session.getMatch().getScore();

        recordScore(finalScore);

        this.gameView.showGameOver(finalScore, correctAnswer);
        this.sessionManager.closeSession();
        this.quizView.showHome();
    }

    /**
     * Handles a won game.
     */
    private void handleGameWon() {
        final QuizSession session = this.sessionManager.getCurrentSession();

        final int finalScore = session.getMatch().getScore();

        recordScore(finalScore);

        this.gameView.showGameWon(finalScore);
        this.sessionManager.closeSession();
        this.quizView.showHome();
    }

    /**
     * Validates the selected answer index.
     *
     * @param answerIndex the selected answer index
     * @param answerCount the number of available answers
     */
    private void validateAnswerIndex(
        final int answerIndex,
        final int answerCount
    ) {
        if (
            answerIndex < 0
            || answerIndex >= answerCount
        ) {
            throw new IllegalArgumentException(
                "Invalid answer index: " + answerIndex
            );
        }
    }

    /**
     * Records the current score in the leaderboard when the active session
     * is not a training session.
     *
     * @param score the final score achieved
     */
    private void recordScore(final int score) {
        if (this.sessionManager.isTrainingSession()) {
            LOGGER.info(
                "Training score not recorded."
            );
            return;
        }

        final String playerName =
            this.sessionManager.getCurrentPlayerName();

        this.leaderboard.recordScore(
            playerName,
            score
        );

        LOGGER.info(
            "Score recorded for: "
                + playerName
                + " - "
                + score
        );
    }
}
