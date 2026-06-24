package it.unibo.view.api;

import java.util.function.Consumer;

/**
 * Interface for the Home View of the Quiz Game. This view is responsible for displaying the initial screen 
 * where the user can enter their name and start the game.
 */
public interface HomeView {

    /**
     * Registers the Info button listener.
     *
     * @param listener the listener to execute
     */
    void setOnInfo(Runnable listener);

    /**
     * Registers the Start button listener.
     *
     * @param listener the listener receiving the player name
     */
    void setOnStart(Consumer<String> listener);

    /**
     * Registers the Leaderboard button listener.
     *
     * @param listener the listener to execute
     */
    void setOnLeaderboard(Runnable listener);

    /**
     * Registers the Exit button listener.
     *
     * @param listener the listener to execute
     */
    void setOnExit(Runnable listener);

    /**
     * Registers the Training button listener.
     *
     * @param listener the listener receiving the player name
     */
    void setOnTraining(Consumer<String> listener);
}
