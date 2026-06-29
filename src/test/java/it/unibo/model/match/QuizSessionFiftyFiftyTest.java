package it.unibo.model.match;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import it.unibo.model.answer.Answer;
import it.unibo.model.data.question.QuestionLoadingException;
import it.unibo.model.data.question.api.QuestionDataRepository;

/**
 * Tests for the fifty-fifty lifeline functionality in the quiz session.
 */
final class QuizSessionFiftyFiftyTest {

    @Test
    void testUseFiftyFiftySuccess() throws QuestionLoadingException {
        final QuestionDataRepository repository = () -> QuizSessionTest.generateQuestionsDTO(QuizSessionTest.TOTAL_QUESTIONS);
        final QuizSessionImpl session = new QuizSessionImpl(repository);
        session.startNewGame();

        session.useFiftyFifty();
        assertEquals(2, session.getDisabledAnswers().size());

        final Answer correctAnswer = session.getCurrentQuestion().getAnswers().stream()
                .filter(Answer::isCorrect)
                .findFirst()
                .orElseThrow();

        session.submitAnswer(correctAnswer);
        assertEquals(1, session.getMatch().getScore(), 
        "After submitting a correct answer, the score should be incremented by 1");
        assertEquals(MatchState.IN_PROGRESS, session.getMatch().getState(), 
        "After submitting a correct answer, the match state should still be IN_PROGRESS");
        assertEquals(1, session.getMatch().getQuestionNumber(), 
        "After submitting a correct answer, the question number should be incremented by 1");
        assertTrue(session.getDisabledAnswers().isEmpty(),
        "After submitting an answer, the disabled answers list should be cleared");
    }

    @Test
    void testUseFiftyFiftyOneTime() throws QuestionLoadingException {
        final QuestionDataRepository repository = () -> QuizSessionTest.generateQuestionsDTO(QuizSessionTest.TOTAL_QUESTIONS);
        final QuizSessionImpl session = new QuizSessionImpl(repository);
        assertThrows(IllegalStateException.class, session::useFiftyFifty,
        "Using fifty-fifty before starting a new game should throw an IllegalStateException");
        session.startNewGame();

        // use 50:50 legally
        session.useFiftyFifty();
        final List<Answer> disabledAnswers = session.getDisabledAnswers();
        final Answer correctAnswer = session.getCurrentQuestion().getAnswers().stream()
                .filter(Answer::isCorrect)
                .findFirst()
                .orElseThrow();

        // use 50:50 a second time in the same question, not legal
        session.useFiftyFifty();
        assertEquals(disabledAnswers, session.getDisabledAnswers(),
        "Using fifty-fifty a second time should not change the disabled answers list");

        session.submitAnswer(correctAnswer);

        // use a 50:50 in another question, not legal because it can be used only once per match
        session.getMatch().nextQuestion();
        session.useFiftyFifty();
        assertTrue(session.getDisabledAnswers().isEmpty(),
        "After using fifty-fifty on the second question, the disabled answers list should be empty");
    }
}
