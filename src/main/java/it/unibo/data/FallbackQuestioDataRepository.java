package it.unibo.data;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import it.unibo.model.data.QuestionDTO;
import it.unibo.model.data.QuestionLoadingException;
import it.unibo.model.data.api.QuestionDataRepository;

public class FallbackQuestioDataRepository implements QuestionDataRepository {

    private static final Logger LOGGER = Logger.getLogger(FallbackQuestioDataRepository.class.getName());
    private final QuestionDataRepository primary;
    private final QuestionDataRepository fallback;

    public FallbackQuestioDataRepository(QuestionDataRepository primary, QuestionDataRepository fallback) {
        this.primary = Objects.requireNonNull(primary);
        this.fallback = Objects.requireNonNull(fallback);
    }
    
    @Override
    public List<QuestionDTO> loadQuestions() throws QuestionLoadingException {
        try {
            return this.primary.loadQuestions();
        } catch(final QuestionLoadingException e) {
            LOGGER.warning("Primary repository failed to load questions, falling back to local repository. Error: " + e.getMessage());
            return this.fallback.loadQuestions();
        }
    }
}
