package it.unibo.view.api;

/**
 * Defines the operations of the main application window.
 * 
 * <p>
 * The main View is responsible for navigation between
 * the Home screen and the Game screen.
 * </p>
 */
public interface QuizView {

    /**
     * Returns the Home View.
     *
     * @return the Home View
     */
    HomeView getHomeView();

    /**
     * Returns the Game View.
     *
     * @return the Game View
     */
    GameView getGameView();

    /**
     * Shows the Home screen.
     */
    void showHome();

    /**
     * Shows the Game screen.
     */
    void showGame();

    /**
     * Displays the main application window.
     */
    void display();

    /**
     * Closes the main application window.
     */
    void close();
}
