package it.unibo.view;

import it.unibo.view.swing.SwingQuizView;

/**
 * 
 */
public class ViewFactory {
    public static QuizView createQuizView() {
        return new SwingQuizView();
    }
}