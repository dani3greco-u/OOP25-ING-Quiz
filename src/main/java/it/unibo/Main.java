package it.unibo;

import javax.swing.SwingUtilities;

import it.unibo.common.LoggingConfiguration;
import it.unibo.controller.QuizController;
import it.unibo.data.question.FallbackQuestionDataRepository;
import it.unibo.data.question.LocalQuestionDataRepository;
import it.unibo.data.question.RemoteQuestionDataRepository;
import it.unibo.model.match.factory.QuizSessionFactory;
import it.unibo.model.data.question.api.QuestionDataRepository;
import it.unibo.model.match.QuizSessionFactoryImpl;
import it.unibo.view.ViewFactory;
import it.unibo.view.api.QuizView;

/**
 * Application entry point.
 */
public final class Main {

    private static final String QUESTIONS_FILE = "/demo.json";
    private static final String REMOTE_QUESTIONS_URL = "https://opentdb.com/api.php?amount=50&category=18&type=multiple";

    /**
     * Private constructor because this class
     * must not be instantiated.
     */
    private Main() {
    }

    /**
     * Starts the application.
     *
     * @param args command-line arguments
     */
    public static void main(final String[] args) {
        LoggingConfiguration.configure();
        SwingUtilities.invokeLater(() -> {

        final QuestionDataRepository repository =
            new FallbackQuestionDataRepository(
                new LocalQuestionDataRepository(QUESTIONS_FILE),
                new RemoteQuestionDataRepository(REMOTE_QUESTIONS_URL)
            );

            final QuizSessionFactory sessionFactory =
                new QuizSessionFactoryImpl(repository);

            final QuizView view =
                ViewFactory.createQuizView();

            new QuizController(
                view,
                sessionFactory
            );

            view.display();
        });
    }
}
