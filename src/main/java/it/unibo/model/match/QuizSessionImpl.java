package it.unibo.model.match;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.unibo.data.QuestionDTO;
import it.unibo.data.QuestionLoadingException;
import it.unibo.data.api.QuestionDataRepository;
import it.unibo.model.answer.Answer;
import it.unibo.model.help.DoubleChance;
import it.unibo.model.help.FiftyFifty;
import it.unibo.model.match.api.QuizSession;
import it.unibo.model.question.Question;
import it.unibo.model.question.factory.QuestionFactory;

public class QuizSessionImpl implements QuizSession{

    private static final int TOTAL_QUESTIONS = 15;
    private final Match match;
    private final List<Question> sessionQuestions;
    private Question currentQuestion;
    private final QuestionDataRepository repository;
    
    //for 50:50
    private final FiftyFifty fiftyFifty;
    private List<Answer> disabledAnswers;

    private DoubleChance doubleChance; 
    private boolean doubleChanceActive;
    
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
        
        this.fiftyFifty = new FiftyFifty();
        this.disabledAnswers = new ArrayList<>();

        this.doubleChance = new DoubleChance();
        this.doubleChanceActive = false;
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
        this.currentQuestion = this.sessionQuestions.get(this.match.getQuestionNumber());
        this.match.start();
    }

    /**
     * @inerithDoc
     */
    @Override
    public void submitAnswer(final Answer answer) {
        if (this.match.getState() != MatchState.IN_PROGRESS) {
            throw new IllegalStateException("Cannot submit an answer when the match is not in progress.");
        }
        if (answer.isCorrect()) {
            this.match.submitAnswer(true);
            //check if the match is still in progress before updating the current question
            if(this.match.getState() == MatchState.IN_PROGRESS) {
                this.match.nextQuestion();
                this.currentQuestion = this.sessionQuestions.get(this.match.getQuestionNumber());
                this.disabledAnswers = new ArrayList<>();
                this.doubleChanceActive = false;
            }
        } else if(this.doubleChanceActive) {
            this.doubleChanceActive = false;
            this.disabledAnswers.add(answer);
        } else {
            this.match.submitAnswer(false);
        }
    }

    /**
     * @inerithDoc
     */
    @Override
    public Match getMatch() {
        return this.match;
    }

    /**
     * @inerithDoc
     */
    @Override
    public Question getCurrentQuestion() {
        if (this.match.getState() != MatchState.IN_PROGRESS) {
            throw new IllegalStateException("Cannot get the current question when the match is not in progress.");
        }
        return this.currentQuestion;
    }

    /**
     * @inerithDoc
     */
    @Override
    public void useFiftyFifty() {
        if (this.match.getState() != MatchState.IN_PROGRESS) {
            throw new IllegalStateException("Cannot use the fifty-fifty lifeline when the match is not in progress.");
        }
        if(this.fiftyFifty.canUse()) {
            // i need a mutable list 
           this.disabledAnswers = new ArrayList<>(this.fiftyFifty.applyHelp(this.currentQuestion));
        } 
    }

    /**
     * @inerithDoc
     */
    @Override
    public List<Answer> getDisabledAnswers() {
        return new ArrayList<>(this.disabledAnswers);
    }

    /**
     * @inerithDoc
     */
    @Override
    public void useDoubleChance() {
        if (this.match.getState() != MatchState.IN_PROGRESS) {
            throw new IllegalStateException("Cannot use the double chance lifeline when the match is not in progress.");
        }
        if(this.doubleChance.canUse()) {
            this.doubleChance.applyHelp(this.currentQuestion);
            this.doubleChanceActive = true;
        }
    }

    /** 
     * @inerithDoc
     */
    @Override
    public boolean isDoubleChanceActive() {
        return this.doubleChanceActive;
    } 
}
