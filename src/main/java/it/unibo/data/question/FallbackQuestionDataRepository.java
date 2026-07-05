package it.unibo.data.question;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import it.unibo.model.data.question.QuestionDTO;
import it.unibo.model.data.question.QuestionLoadingException;
import it.unibo.model.data.question.api.QuestionDataRepository;

/** 
 * A QuestionDataRepository implementation that tries to load questions from a primary repository and falls back 
 * to a secondary repository if the primary fails.
 */

public class FallbackQuestionDataRepository implements QuestionDataRepository {

    private static final Logger LOGGER = Logger.getLogger(FallbackQuestionDataRepository.class.getName());
    private final QuestionDataRepository primary;
    private final QuestionDataRepository fallback;

    /**
     * Creates a new FallbackQuestionDataRepository with the specified primary and fallback repositories.
     * 
     * @param primary the primary QuestionDataRepository to load questions from
     * @param fallback the fallback QuestionDataRepository to load questions from if the primary fails
     */
    public FallbackQuestionDataRepository(final QuestionDataRepository primary, final QuestionDataRepository fallback) {
        this.primary = Objects.requireNonNull(primary);
        this.fallback = Objects.requireNonNull(fallback);
    }

    /**
     * Tries to load questions from the primary repository. If it fails, 
     * logs a warning and tries to load from the fallback repository.
     */
    @Override
    public List<QuestionDTO> loadQuestions() throws QuestionLoadingException {
        try {
            return this.primary.loadQuestions();
        } catch (final QuestionLoadingException primaryException) {
            LOGGER.warning(
                "Primary repository failed to load questions. "
                + "Trying fallback repository. Error: "
                + primaryException.getMessage()
            );

            try {
                return this.fallback.loadQuestions();
            } catch (final QuestionLoadingException fallbackException) {
                fallbackException.addSuppressed(primaryException);
                throw fallbackException;
            }
        }
    }
}
