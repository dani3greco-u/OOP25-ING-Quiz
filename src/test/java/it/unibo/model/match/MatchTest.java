package it.unibo.model.match;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.common.Observer;

/**
 * Test class for the Match class.
 */
final class MatchTest {

    private Match match;
    private TestObserver observer;

    @BeforeEach
    public void setUp() {
        this.match = new Match();
        this.observer = new TestObserver();
        this.match.addObserver(this.observer);
    }

    @Test
    void testInitialState() {
        assertEquals(MatchState.NOT_STARTED, this.match.getState());
        assertEquals(0, this.match.getScore());
        assertEquals(0, this.match.getQuestionNumber());
        assertEquals(0, this.observer.getUpdates());
    }

    @Test
    void testStartChangesStateAndNotifies() {
        this.match.start();

        assertEquals(MatchState.IN_PROGRESS, this.match.getState());
        assertEquals(0, this.match.getQuestionNumber());
        assertEquals(1, this.observer.getUpdates());

        final MatchEvent event = this.observer.getLastEvent();
        assertEquals(MatchEventType.MATCH_STARTED, event.type());
        assertEquals(MatchState.IN_PROGRESS, event.state());
        assertEquals(0, event.score());
        assertEquals(0, event.questionNumber());
    }

    @Test
    void testSubmitCorrectAnswerUpdatesScoreAndNotifies() {
        this.match.start();
        this.match.submitAnswer(true);

        assertEquals(MatchState.IN_PROGRESS, this.match.getState());
        assertEquals(1, this.match.getScore());
        // one for start, one for submitAnswer
        assertEquals(2, this.observer.getUpdates());

        final MatchEvent event = this.observer.getLastEvent();
        assertEquals(MatchEventType.CORRECT_ANSWER, event.type());
        assertEquals(MatchState.IN_PROGRESS, event.state());
        assertEquals(1, event.score());
        assertEquals(0, event.questionNumber());
    }

    @Test
    void testSubmitWrongAnswerLosesMatchAndNotifies() {
        this.match.start();
        this.match.submitAnswer(false);

        assertEquals(MatchState.LOSE, this.match.getState());
        assertEquals(0, this.match.getScore());
        assertEquals(2, this.observer.getUpdates());

        final MatchEvent event = this.observer.getLastEvent();
        assertEquals(MatchEventType.MATCH_LOST, event.type());
        assertEquals(MatchState.LOSE, event.state());
        assertEquals(0, event.score());
        assertEquals(0, event.questionNumber());
    }

    @Test
    void testNextQuestionAdvancesAndNotifies() {
        this.match.start();
        this.match.submitAnswer(true);
        this.match.nextQuestion();

        assertEquals(1, this.match.getQuestionNumber());
        // one for start, one for submitAnswer, one for nextQuestion
        assertEquals(3, this.observer.getUpdates());

        final MatchEvent event = this.observer.getLastEvent();
        assertEquals(MatchEventType.QUESTION_CHANGED, event.type());
        assertEquals(MatchState.IN_PROGRESS, event.state());
        assertEquals(1, event.score());
        assertEquals(1, event.questionNumber());
    }

    @Test
    void testWinMatchNotifies() {
        this.match.start();
        this.match.submitAnswer(true);
        this.match.nextQuestion();
        this.match.win();

        assertEquals(1, this.match.getQuestionNumber());
        // one for start, one for submitAnswer, one for nextQuestion
        assertEquals(4, this.observer.getUpdates());

        final MatchEvent event = this.observer.getLastEvent();
        assertEquals(MatchEventType.MATCH_WON, event.type());
        assertEquals(MatchState.WIN, event.state());
        assertEquals(1, event.score());
        assertEquals(1, event.questionNumber());
    }

    @Test
    void testSubmitAnswerBeforeStart() {
        assertThrows(IllegalStateException.class, () -> this.match.submitAnswer(true));
    }

    @Test
    void testNextQuestionBeforeStart() {
        assertThrows(IllegalStateException.class, this.match::nextQuestion);
    }

    @Test
    void testCannotStartTwice() {
        this.match.start();
        assertThrows(IllegalStateException.class, this.match::start);
    }

    @Test
    void testCannotAdvanceAfterLoss() {
        this.match.start();
        this.match.submitAnswer(false);

        assertThrows(IllegalStateException.class, this.match::nextQuestion);
    }

    /**
     * A simple test observer that records the last event it received and the number of updates.
     */
    private final class TestObserver implements Observer<MatchEvent> {
        private MatchEvent lastEvent;
        private int updates;

        @Override
        public void update(final MatchEvent notify) {
            this.lastEvent = notify;
            this.updates++;
        }

        public MatchEvent getLastEvent() {
            return this.lastEvent;
        }

        public int getUpdates() {
            return this.updates;
        }
    }
}
