package it.unibo.model.match;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import it.unibo.common.Observer;
import it.unibo.model.match.api.MatchSubject;

/**
 * The Match class represents a game match, managing its state, score, and the current question number. 
 * It implements the MatchSubject interface to allow observers to subscribe and receive updates about match events. 
 * The class provides methods to start the match, submit answers, move to the next question, and win the match. 
 * It also includes error handling to ensure that actions are performed in the correct state of the match.
 */
public final class Match implements MatchSubject {

    private final List<Observer<MatchEvent>> observers;
    private MatchState state;
    private int score;
    private int questionNumber;

    /**
     * Constructor for the Match class. Initializes the match with default values and an empty list of observers.
     */
    public Match() {
        this.observers = new ArrayList<>();
        this.state = MatchState.NOT_STARTED;
        this.score = 0;
        this.questionNumber = 0;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void addObserver(final Observer<MatchEvent> observer) {
        this.observers.add(Objects.requireNonNull(observer));
    }

    /**
     * @inheritDoc
     */
    @Override
    public void removeObserver(final Observer<MatchEvent> observer) {
        this.observers.remove(Objects.requireNonNull(observer));
    }

    /**
     * @inheritDoc
     */
    @Override
    public void notifyObservers(final MatchEvent notify) {
        Objects.requireNonNull(notify);
        for (final Observer<MatchEvent> observer : observers) {
            observer.update(notify);
        }
    }

    /**
     * Starts the match.
     * 
     * @throws IllegalStateException if the match has already started
     */
    public void start() {
        if (this.state != MatchState.NOT_STARTED) {
            throw new IllegalStateException("Match already started");
        }
        this.state = MatchState.IN_PROGRESS;
        this.questionNumber = 1;
        notifyObservers(new MatchEvent(
            MatchEventType.MATCH_STARTED,
            this.state,
            this.score,
            this.questionNumber
        ));
    }

    /**
     * Submits an answer for the current question.
     * 
     * @param isCorrect indicates whether the submitted answer is correct or not
     */
    public void submitAnswer(final boolean isCorrect) {
        this.ensureInProgress();
        if (isCorrect) {
            this.score++;
            notifyObservers(new MatchEvent(
                MatchEventType.CORRECT_ANSWER,
                this.state,
                this.score,
                this.questionNumber
            ));
        } else {
            this.state = MatchState.LOSE;
            notifyObservers(new MatchEvent(
                MatchEventType.MATCH_LOST,
                this.state,
                this.score,
                this.questionNumber
            ));
        }
    }

    /**
     * Moves to the next question in the match.
     * 
     */
    public void nextQuestion() {
        this.ensureInProgress();
        this.questionNumber++;
        this.notifyObservers(new MatchEvent(
            MatchEventType.QUESTION_CHANGED,
            this.state,
            this.score,
            this.questionNumber
        ));
    }

    /**
     * Marks the match as won.
     */
    public void win() {
        this.ensureInProgress();
        this.state = MatchState.WIN;
        this.notifyObservers(new MatchEvent(
            MatchEventType.MATCH_WON,
            this.state,
            this.score,
            this.questionNumber
        ));
    }

    /**
     * Ensures that the match is in progress.
     * 
     * @throws IllegalStateException if the match is not in progress
     */
    private void ensureInProgress() {
        if (this.state != MatchState.IN_PROGRESS) {
            throw new IllegalStateException("Match not in progress");
        }
    }

    /**
     * Returns an unmodifiable copy of the list of observers for this match.
     *
     * @return the list of observers
     */
    public List<Observer<MatchEvent>> getObservers() {
        return Collections.unmodifiableList(new ArrayList<>(this.observers));
    }

    /**
     * Returns the state of the match.
     *
     * @return the state of the match
     */
    public MatchState getState() {
        return this.state;
    }

    /**
     * Returns the score of the match.
     *
     * @return the score of the match
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Returns the number of the current question in the match.
     * 
     * @return the number of the current question in the match
     */
    public int getQuestionNumber() {
        return this.questionNumber;
    }
}
