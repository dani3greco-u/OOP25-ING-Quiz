package it.unibo.model.match;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import it.unibo.data.QuestionDTO;
import it.unibo.data.QuestionLoadingException;
import it.unibo.data.api.QuestionDataRepository;
import it.unibo.model.answer.Answer;
import it.unibo.model.question.Difficulty;
import it.unibo.model.question.Question;

final class QuizSessionTest {
    
    /**
     * Helper method to generate a list of QuestionDTOs for testing purposes.
     * 
     * @param amount the number of QuestionDTOs to generate
     * @return a list of generated QuestionDTOs
     */
    static List<QuestionDTO> generateQuestionsDTO(final int amount) {
        final List<QuestionDTO> dtos = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            dtos.add(new QuestionDTO(
                "multiple",
                Difficulty.EASY,
                "Science: Computers",
                "Question " + (i + 1),
                "Correct Answer",
                List.of("Wrong Answer 1", "Wrong Answer 2", "Wrong Answer 3")
            ));
        }
        return dtos;
    }

    /**
     * Tests the startNewGame method of the QuizSessionImpl class to ensure that it correctly initializes 
     * a new game session and updates the match state accordingly.
     * 
     * @throws QuestionLoadingException if there is an error loading questions from the repository
     */
    @Test
    void testStartNewGameSuccess() throws QuestionLoadingException {

        final QuestionDataRepository repository = () -> generateQuestionsDTO(15);
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
        // it's the same test with more than 15 questions because the implementation requires exactly 15 questions
        final QuestionDataRepository repository = () -> generateQuestionsDTO(10);
        final QuizSessionImpl session = new QuizSessionImpl(repository);
        assertEquals(MatchState.NOT_STARTED, session.getMatch().getState(), 
        "Before starting a new game, the match state should be NOT_STARTED");

        assertThrows(IllegalStateException.class, () -> session.startNewGame());
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

        assertThrows(QuestionLoadingException.class, () -> session.startNewGame());
        assertEquals(MatchState.NOT_STARTED, session.getMatch().getState(), 
        "If starting a new game fails the match state should remain NOT_STARTED");
    }

    @Test
    void  testSubmitAnswerSuccess() throws QuestionLoadingException {
        final QuestionDataRepository repository = () -> generateQuestionsDTO(15);
        final QuizSessionImpl session = new QuizSessionImpl(repository);
        session.startNewGame();

        Question question = session.getCurrentQuestion();
        Answer correctAnswer = question.getAnswers().stream()
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

        Answer wrongAnswer = question.getAnswers().stream()
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
}
