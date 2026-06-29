package it.unibo.model.match;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import it.unibo.model.answer.Answer;
import it.unibo.model.data.question.QuestionLoadingException;
import it.unibo.model.data.question.api.QuestionDataRepository;
import it.unibo.model.question.Difficulty;
import it.unibo.model.question.Question;

/**
 * Test class for the Switch help strategy in the QuizSessionImpl class.
 */
final class QuizSessionSwitchTest {

    /**
     * Tests the useSwitch method of the QuizSessionImpl class to ensure that it correctly allows the player 
     * to switch the current question with a new one of the same difficulty and updates the match state accordingly.
     * 
     * @throws QuestionLoadingException if there is an error loading the questions from the repository
     */
    @Test
    void testSwitchSuccess() throws QuestionLoadingException {
       final QuestionDataRepository repository = () -> QuizSessionTest.generateQuestionsDTO(QuizSessionTest.TOTAL_QUESTIONS);
        final QuizSessionImpl session = new QuizSessionImpl(repository);
        session.startNewGame();
        final Difficulty difficulty = session.getCurrentQuestion().getDifficulty();
        final Question switchedQuestion = session.getCurrentQuestion();
        session.useSwitch();
        assertEquals(difficulty, session.getCurrentQuestion().getDifficulty(),
        "After using switch, the difficulty of the current question should remain the same");
        assertNotEquals(switchedQuestion, session.getCurrentQuestion(),
        "After using switch, the current question should be different");
        assertTrue(session.getDisabledAnswers().isEmpty(),
        "After using switch, the disabled answers list should be empty");
    }

    /**
     * Tests the useSwitch method of the QuizSessionImpl class to ensure that after using the switch help, 
     * it cannot be used again in the same match.
     * 
     * @throws QuestionLoadingException if there is an error loading the questions from the repository
     */
    @Test
    void testSwitchOneTime() throws QuestionLoadingException {
        final QuestionDataRepository repository = () -> QuizSessionTest.generateQuestionsDTO(QuizSessionTest.TOTAL_QUESTIONS);
        final QuizSessionImpl session = new QuizSessionImpl(repository);
        session.startNewGame();
        session.useSwitch();
        session.submitAnswer(session.getCurrentQuestion().getAnswers().stream()
                .filter(Answer::isCorrect)
                .findFirst()
                .orElseThrow());
        assertFalse(session.getSwitchHelp().canUse(),
        "After using switch, the switch help should not be usable anymore");
    }
}
