package it.unibo.view.api;

import java.util.function.Consumer;

/**
 * Interface for the Home View of the Quiz Game. This view is responsible for displaying the initial screen 
 * where the user can enter their name and start the game.
 */
public interface HomeView {

    /**
     * 
     */
    void setOnInfo(Runnable listener);

    /**
     * 
     */
    void setOnStart(Consumer<String> listener);

    /**
     * 
     */
    void setOnLeaderboard(Runnable listener);

    /**
     * 
     */
    void setOnExit(Runnable listener);

    /**
     * 
     */
    void setOnTraining(Consumer<String> listener);
}
