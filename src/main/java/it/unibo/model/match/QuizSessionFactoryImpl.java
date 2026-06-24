package it.unibo.model.match;

import java.util.Objects;

import it.unibo.model.data.api.QuestionDataRepository;
import it.unibo.model.match.api.QuizSession;
import it.unibo.model.match.factory.QuizSessionFactory;

/**
 * Default implementation of {@link QuizSessionFactory}.
 */
public final class QuizSessionFactoryImpl implements QuizSessionFactory {

    private final QuestionDataRepository repository;

    /**
     * Creates a new quiz session factory.
     *
     * @param repository repository used to load the questions
     */
    public QuizSessionFactoryImpl(
        final QuestionDataRepository repository
    ) {
        this.repository = Objects.requireNonNull(
            repository,
            "The question repository cannot be null"
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuizSession createSession() {
        return new QuizSessionImpl(this.repository);
    }
}
