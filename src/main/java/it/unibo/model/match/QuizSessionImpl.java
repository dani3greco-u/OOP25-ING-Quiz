package it.unibo.model.match;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import it.unibo.data.QuestionDTO;
import it.unibo.data.QuestionLoadingException;
import it.unibo.data.api.QuestionDataRepository;
import it.unibo.model.answer.Answer;
import it.unibo.model.help.DoubleChance;
import it.unibo.model.help.FiftyFifty;
import it.unibo.model.help.Switch;
import it.unibo.model.match.api.QuizSession;
import it.unibo.model.question.Difficulty;
import it.unibo.model.question.Question;
import it.unibo.model.question.factory.QuestionFactory;

public class QuizSessionImpl implements QuizSession{

    private static final int TOTAL_QUESTIONS = 15;
    private static final int RESERVE_QUESTIONS = 3;

    private final Match match;
    private final List<Question> sessionQuestions;
    private Question currentQuestion;
    private final QuestionDataRepository repository;
    
    //for 50:50
    private final FiftyFifty fiftyFifty;
    private List<Answer> disabledAnswers;

    //for double chance
    private final DoubleChance doubleChance; 
    private boolean doubleChanceActive;

    // for switch
    private final Switch switchHelp;
    // 1 reserve question for each difficulty level
    private final Map<Difficulty, Question> reserveQuestions; 

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
        
        this.switchHelp = new Switch();
        this.reserveQuestions = new HashMap<Difficulty,Question>();
        
    }

    /**
     * @inerithDoc
     */
    @Override
    public void startNewGame() throws QuestionLoadingException {
        this.sessionQuestions.clear();
        final List<QuestionDTO> dtos = this.repository.loadQuestions();
        if(dtos.size() != TOTAL_QUESTIONS + RESERVE_QUESTIONS) {
            throw new IllegalStateException("The repository did not provide enough questions. Expected at least " 
                                            + (TOTAL_QUESTIONS + RESERVE_QUESTIONS) + " but got " + dtos.size());
        }
        
        final QuestionFactory factory = new QuestionFactory();
        for(final QuestionDTO dto : dtos) {
            final Question question = factory.fromDTO(dto);
            if(!this.reserveQuestions.containsKey(question.getDifficulty())) {
                this.reserveQuestions.put(question.getDifficulty(), question);
            } else {
                this.sessionQuestions.add(question); 
            }
        }
        
        if (this.sessionQuestions.size() != TOTAL_QUESTIONS || this.reserveQuestions.size() != RESERVE_QUESTIONS) {
            throw new IllegalStateException("The repository did not provide the correct number of questions");
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

    /**
     * @inerithDoc
     */
    @Override
    public void useSwitch() {
        if (this.match.getState() != MatchState.IN_PROGRESS) {
            throw new IllegalStateException("Cannot use the switch lifeline when the match is not in progress.");
        }
        if(this.switchHelp.canUse()) {
            this.switchHelp.applyHelp(this.currentQuestion);
            final Difficulty currentDifficulty = this.currentQuestion.getDifficulty();
            final Question reserve = this.reserveQuestions.get(currentDifficulty);
            if(reserve == null) {
                throw new IllegalStateException("No reserve question available for difficulty: " + currentDifficulty);
            }

            this.currentQuestion = reserve;
            this.disabledAnswers = new ArrayList<>();
            this.doubleChanceActive = false;
        }
    }

    /**
     * @inerithDoc
     */
    @Override
    public Switch getSwitchHelp() {
        return this.switchHelp;
    }

}
