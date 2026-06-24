package it.unibo.view;

import it.unibo.view.api.HomeView;

import java.util.List;
import java.util.function.Consumer;

public interface QuizView {

    HomeView getHomeView();

    
    void showHome();

    void showGame();

    void setQuestionText(String text);

    void setAnswers(List<String> answers);

    void updateProgress(
        int currentQuestion,
        int totalQuestions,
        int currentPrize
    );

    void disableAnswer(int index);

    void setAnswerListener(Consumer<Integer> listener);

    void showCorrectAnswer();

    void showWrongAnswer();

    void display();
}