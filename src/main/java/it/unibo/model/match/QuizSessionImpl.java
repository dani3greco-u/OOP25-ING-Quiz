package it.unibo.model.match;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.unibo.data.QuestionDTO;
import it.unibo.data.QuestionLoadingException;
import it.unibo.data.RemoteQuestionDataRepository;
import it.unibo.data.api.QuestionDataRepository;
import it.unibo.model.match.api.QuizSession;
import it.unibo.model.question.Question;
import it.unibo.model.question.factory.QuestionFactory;

public class QuizSessionImpl implements QuizSession{

    private static final int TOTAL_QUESTIONS = 15;
    private final Match match;
    private final List<Question> sessionQuestions;
    private Question currentQuestion;
    private final QuestionDataRepository repository;
    
    /**
     * Constructor for the QuizSessionImpl class.
     * 
     * @param repository the QuestionDataRepository to load questions from
     */
    public QuizSessionImpl(final QuestionDataRepository repository)
    {
        this.repository = Objects.requireNonNull(repository);
        this.match = new Match();
        this.sessionQuestions = new ArrayList<>();
    }

    /**
     * @inerithDoc
     */
    @Override
    public void startNewGame() throws QuestionLoadingException {
        this.sessionQuestions.clear();

        final List<QuestionDTO> dtos = this.repository.loadQuestions();

        final QuestionFactory factory = new QuestionFactory();

        for(final QuestionDTO dto : dtos) {
            this.sessionQuestions.add(factory.fromDTO(dto));
        }
        
        if (this.sessionQuestions.size() != TOTAL_QUESTIONS) {
            throw new IllegalStateException("The repository did not provide the correct number of questions. Expected " 
                                            + TOTAL_QUESTIONS + " but got " + this.sessionQuestions.size());
        }

        this.currentQuestion = this.sessionQuestions.get(match.getQuestionNumber());
    
        match.start();
    }

    /**
     * Returns the current match instance.
     * 
     * @return the current match instance
     */
    public Match getMatch() {
        return this.match;
    }
}
