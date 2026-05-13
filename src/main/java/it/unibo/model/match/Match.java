package it.unibo.model.match;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import it.unibo.common.Observer;

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
    public void addObserver(Observer<MatchEvent> observer) {
        this.observers.add(Objects.requireNonNull(observer));
    }
    
    /**
     * @inheritDoc
     */
    @Override
    public void removeObserver(Observer<MatchEvent> observer) {
        this.observers.remove(Objects.requireNonNull(observer));
    }
    
    /**
     * @inheritDoc
     */
    @Override
    public void notifyObservers(MatchEvent notify) {
        Objects.requireNonNull(notify);
        for (Observer<MatchEvent> observer : observers) {
            observer.update(notify);
        }
    }

    /**
     * Starts the match.
     * 
     * @throws IllegalStateException if the match has already started
     */
    public void start() {
        if(this.state != MatchState.NOT_STARTED) {
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
     * 
     * @param isCorrect
     */
    public void submitAnswer(boolean isCorrect) {
        if(this.state != MatchState.IN_PROGRESS) {
            throw new IllegalStateException("Match not started");
        }

        if(isCorrect) {
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
     * 
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

    private void ensureInProgress() {
        if(this.state != MatchState.IN_PROGRESS) {
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
