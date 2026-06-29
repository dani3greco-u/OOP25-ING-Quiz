package it.unibo.model.match;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import it.unibo.model.answer.Answer;
import it.unibo.model.data.question.QuestionLoadingException;
import it.unibo.model.data.question.api.QuestionDataRepository;

/**
 * Tests for the double chance lifeline functionality in the quiz session.
 */
final class QuizSessionDoubleChanceTest {

    /**
     * Tests the useDoubleChance method of the QuizSessionImpl class to ensure that it correctly allows the player 
     * to have two attempts to answer a question correctly and updates the match state accordingly.
     * 
     * @throws QuestionLoadingException if there is an error loading questions from the repository
     */
    @Test
    void testUseDoubleChanceSuccess() throws QuestionLoadingException {
        final QuestionDataRepository repository = () -> QuizSessionTest.generateQuestionsDTO(QuizSessionTest.TOTAL_QUESTIONS);
        final QuizSessionImpl session = new QuizSessionImpl(repository);
        assertEquals(MatchState.NOT_STARTED, session.getMatch().getState(), 
        "Before starting a new game, the match state should be NOT_STARTED");

        session.startNewGame();
        assertEquals(MatchState.IN_PROGRESS, session.getMatch().getState(), 
        "After starting a new game, the match state should be IN_PROGRESS");

        session.useDoubleChance();
        assertTrue(session.isDoubleChanceActive(),
        "After using double chance, the double chance should be active");
        // submit a wrong answer
        final Answer wrongAnswer = session.getCurrentQuestion().getAnswers().stream()
                .filter(answer -> !answer.isCorrect())
                .findFirst()
                .orElseThrow();
        session.submitAnswer(wrongAnswer);

        assertEquals(0, session.getMatch().getScore(), 
        "After using double chance, the score should still be 0");
        assertEquals(MatchState.IN_PROGRESS, session.getMatch().getState(), 
        "After using double chance, the match state should still be IN_PROGRESS");
        assertEquals(0, session.getMatch().getQuestionNumber(), 
        "After using double chance, the question number should still be 0");
        assertEquals(1, session.getDisabledAnswers().size(),
        "After using double chance, one answer should be disabled");
        assertTrue(session.getDisabledAnswers().contains(wrongAnswer),
        "After using double chance, the submitted wrong answer should be disabled");
    }

    /**
     * Tests that the useDoubleChance method can only be used once.
     * 
     * @throws QuestionLoadingException if there is an error loading questions from the repository
     */
    @Test
    void testUseDoubleChanceOneTime() throws QuestionLoadingException {
        final QuestionDataRepository repository = () -> QuizSessionTest.generateQuestionsDTO(QuizSessionTest.TOTAL_QUESTIONS);
        final QuizSessionImpl session = new QuizSessionImpl(repository);
        session.startNewGame();
        session.useDoubleChance();
        assertTrue(session.isDoubleChanceActive(),
        "After using double chance, the double chance should be active");
        // submit a wrong answer
        final Answer wrongAnswer = session.getCurrentQuestion().getAnswers().stream()
                .filter(answer -> !answer.isCorrect())
                .findFirst()
                .orElseThrow();
        session.submitAnswer(wrongAnswer);
        assertFalse(session.isDoubleChanceActive(),
        "After submitting a wrong answer with double chance active, the double chance should be deactivated");

        // use double chance a second time in the same question, not legal
        session.useDoubleChance();
                assertEquals(1, session.getDisabledAnswers().size(),
        "After using double chance, one answer should be disabled");
        assertTrue(session.getDisabledAnswers().contains(wrongAnswer),
        "After using double chance, the submitted wrong answer should be disabled");

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

        // use a double chance in another question, not legal because it can be used only once per match
        session.getMatch().nextQuestion();
        assertFalse(session.isDoubleChanceActive(),
        "Before using double chance on the second question, the double chance should not be active");
        session.useDoubleChance();
        assertTrue(session.getDisabledAnswers().isEmpty(),
        "After using double chance on the second question, the disabled answers list should be empty");
    }
}
