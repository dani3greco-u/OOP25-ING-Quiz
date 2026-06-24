package it.unibo.view;

import it.unibo.view.api.QuizView;
import it.unibo.view.swing.SwingQuizView;

/**
 * Factory for creating the application views.
 */
public final class ViewFactory {

    /**
     * Private constructor to prevent instantiation.
     */
    private ViewFactory() {
    }

    /**
     * Creates the main quiz view.
     *
     * @return the quiz view
     */
    public static QuizView createQuizView() {
        return new SwingQuizView();
    }
}
