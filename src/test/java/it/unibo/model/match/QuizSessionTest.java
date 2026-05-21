package it.unibo.model.match;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import it.unibo.data.QuestionDTO;
import it.unibo.data.QuestionLoadingException;
import it.unibo.data.api.QuestionDataRepository;
import it.unibo.model.answer.Answer;
import it.unibo.model.question.Difficulty;
import it.unibo.model.question.Question;

//CHECKSTYLE: MultipleStringLiterals OFF
/**
 * Test class for the QuizSessionImpl class.
 */
final class QuizSessionTest {

    // package-private for testing purposes
    /**
     * The total number of questions required for a valid game session.
     */
    static final int TOTAL_QUESTIONS = 18;

    /**
     * Helper method to generate a list of QuestionDTOs for testing purposes.
     * 
     * @param amount the number of QuestionDTOs to generate
     * @return a list of generated QuestionDTOs
     */
    static List<QuestionDTO> generateQuestionsDTO(final int amount) {
        final int questionsPerDiff = amount / Difficulty.values().length;

        return Arrays.stream(Difficulty.values())
                // for each difficulty, generate the specified number of questions
                .flatMap(difficulty -> IntStream.range(0, questionsPerDiff)
                        .mapToObj(i -> new QuestionDTO(
                                "multiple",
                                difficulty,
                                "Category",
                                "Question " + difficulty.name() + " " + (i + 1),
                                "Correct Answer",
                                List.of("Wrong 1", "Wrong 2", "Wrong 3")
                        )))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Tests the startNewGame method of the QuizSessionImpl class to ensure that it correctly initializes 
     * a new game session and updates the match state accordingly.
     * 
     * @throws QuestionLoadingException if there is an error loading questions from the repository
     */
    @Test
    void testStartNewGameSuccess() throws QuestionLoadingException {

        final QuestionDataRepository repository = () -> generateQuestionsDTO(TOTAL_QUESTIONS);
        final QuizSessionImpl session = new QuizSessionImpl(repository);
        assertEquals(MatchState.NOT_STARTED, session.getMatch().getState(), 
        "Before starting a new game, the match state should be NOT_STARTED");

        session.startNewGame();
        assertEquals(MatchState.IN_PROGRESS, session.getMatch().getState(), 
        "After starting a new game, the match state should be IN_PROGRESS");
        assertEquals(0, session.getMatch().getQuestionNumber(), 
        "After starting a new game, the question number should be 0");
    }

    /**
     * Tests the startNewGame method of the QuizSessionImpl class to ensure that it correctly handles the case where
     * the repository provides an insufficient number of questions.
     * 
     * @throws QuestionLoadingException if there is an error loading questions from the repository
     */
    @Test
    void testStartNewGameInsufficientQuestions() throws QuestionLoadingException {
        // it's the same test with more than 18 questions because the implementation requires exactly 18 questions
        final QuestionDataRepository repository = () -> generateQuestionsDTO(10);
        final QuizSessionImpl session = new QuizSessionImpl(repository);
        assertEquals(MatchState.NOT_STARTED, session.getMatch().getState(), 
        "Before starting a new game, the match state should be NOT_STARTED");

        assertThrows(IllegalStateException.class, session::startNewGame);
        assertEquals(MatchState.NOT_STARTED, session.getMatch().getState(), 
        "If starting a new game fails the match state should remain NOT_STARTED");
    }

    /**
     * Tests the startNewGame method of the QuizSessionImpl class to ensure that it correctly handles the case where
     * the repository throws a QuestionLoadingException.
     * 
     * @throws QuestionLoadingException if there is an error loading questions from the repository
     */
    @Test
    void testStarNewGameRepositoryException() throws QuestionLoadingException {
        final QuestionDataRepository repository = () -> { 
            throw new QuestionLoadingException("Failed to load questions"); 
        };
        final QuizSessionImpl session = new QuizSessionImpl(repository);

        assertEquals(MatchState.NOT_STARTED, session.getMatch().getState(), 
        "Before starting a new game, the match state should be NOT_STARTED");

        assertThrows(QuestionLoadingException.class, session::startNewGame);
        assertEquals(MatchState.NOT_STARTED, session.getMatch().getState(), 
        "If starting a new game fails the match state should remain NOT_STARTED");
    }

    @Test
    void testSubmitAnswerSuccess() throws QuestionLoadingException {
        final QuestionDataRepository repository = () -> generateQuestionsDTO(TOTAL_QUESTIONS);
        final QuizSessionImpl session = new QuizSessionImpl(repository);
        session.startNewGame();

        final Question question = session.getCurrentQuestion();
        final Answer correctAnswer = question.getAnswers().stream()
            .filter(Answer::isCorrect)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No correct answer found for the current question"));

        session.submitAnswer(correctAnswer);
        assertEquals(1, session.getMatch().getScore(), 
        "After submitting a correct answer, the score should be incremented by 1");
        assertEquals(MatchState.IN_PROGRESS, session.getMatch().getState(), 
        "After submitting a correct answer, the match state should still be IN_PROGRESS");
        assertEquals(1, session.getMatch().getQuestionNumber(), 
        "After submitting a correct answer, the question number should be incremented by 1");

        final Answer wrongAnswer = question.getAnswers().stream()
            .filter(answer -> !answer.isCorrect())
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No wrong answer found for the current question"));

        session.submitAnswer(wrongAnswer);
        assertEquals(1, session.getMatch().getScore(), 
        "After submitting a correct answer, the score should be incremented by 1");
        assertEquals(MatchState.LOSE, session.getMatch().getState(), 
        "After submitting a wrong answer, the match state should be LOSE");
        assertEquals(1, session.getMatch().getQuestionNumber(), 
        "After submitting a wrong answer, the question number should not be incremented");
    }

    @Test
    void testUseFiftyFiftyAndDoubleChance() throws QuestionLoadingException {
        final QuestionDataRepository repository = () -> generateQuestionsDTO(TOTAL_QUESTIONS);
        final QuizSessionImpl session = new QuizSessionImpl(repository);
        session.startNewGame();

        // use fifty-fifty
        session.useFiftyFifty();
        assertEquals(2, session.getDisabledAnswers().size(), 
        "After using fifty-fifty, two answers should be disabled");

        // 2 answer remaining, one correct and one wrong
        final Answer correctAnswer = session.getCurrentQuestion().getAnswers().stream()
                .filter(Answer::isCorrect)
                .findFirst()
                .orElseThrow();
        final Answer remainingWrongAnswer = session.getCurrentQuestion().getAnswers().stream()
                .filter(a -> !a.isCorrect() && !session.getDisabledAnswers().contains(a))
                .findFirst()
                .orElseThrow();

        // use double chance
        session.useDoubleChance();
        assertTrue(session.isDoubleChanceActive(), 
        "After using double chance, the double chance should be active");

        // submit the remaining wrong answer
        session.submitAnswer(remainingWrongAnswer);
        assertFalse(session.isDoubleChanceActive(),
        "After submitting a wrong answer with double chance active, the double chance should be deactivated");
        // 2 for the fifty-fifty and 1 for the double chance
        assertEquals(3, session.getDisabledAnswers().size(),
        "After submitting a wrong answer with double chance active, one answer should be disabled");
        assertEquals(MatchState.IN_PROGRESS, session.getMatch().getState(),
        "After submitting a wrong answer with double chance active, the match state should still be IN_PROGRESS");

        // submit the correct answer
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
    void testUseAllHelps() throws QuestionLoadingException {
        final QuestionDataRepository repository = () -> generateQuestionsDTO(TOTAL_QUESTIONS);
        final QuizSessionImpl session = new QuizSessionImpl(repository);
        session.startNewGame();

        // use fifty-fifty
        session.useFiftyFifty();
        assertEquals(2, session.getDisabledAnswers().size(), 
        "After using fifty-fifty, two answers should be disabled");

        // use double chance
        session.useDoubleChance();
        assertTrue(session.isDoubleChanceActive(), 
        "After using double chance, the double chance should be active");

        // use switch
        session.useSwitch();
        assertFalse(session.isDoubleChanceActive(),
        "After using switch, the double chance should be deactivated");
        assertTrue(session.getDisabledAnswers().isEmpty(),
        "After using switch, the disabled answers list should be empty");
    }
}
