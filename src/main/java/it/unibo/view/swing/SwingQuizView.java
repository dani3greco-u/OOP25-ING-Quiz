package it.unibo.view.swing;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.view.api.GameView;
import it.unibo.view.api.HomeView;
import it.unibo.view.api.QuizView;
import it.unibo.view.swing.panels.SwingGamePanel;
import it.unibo.view.swing.panels.SwingHomePanel;

/**
 * Swing implementation of the main application View.
 * 
 * <p>
 * This class is responsible for containing the available screens
 * and switching between them.
 * </p>
 */
public final class SwingQuizView extends JFrame implements QuizView {

    private static final long serialVersionUID = 1L;

    private static final String HOME_CARD = "HOME";
    private static final String GAME_CARD = "GAME";

    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 700;

    private static final int MINIMUM_WIDTH = 600;
    private static final int MINIMUM_HEIGHT = 600;

    private final CardLayout cardLayout;
    private final JPanel mainContainer;

    private final SwingHomePanel homePanel;
    private final SwingGamePanel gamePanel;

    /**
     * Creates the main application window.
     */
    public SwingQuizView() {
        super("Chi Vuol Essere Ingegnere?");

        this.cardLayout = new CardLayout();
        this.mainContainer = new JPanel(this.cardLayout);

        this.homePanel = new SwingHomePanel();
        this.gamePanel = new SwingGamePanel();

        configureFrame();
        configureScreens();
    }

    /**
     * Configures the main frame.
     */
    private void configureFrame() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setMinimumSize(
            new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT)
        );
    }

    /**
     * Adds the available screens to the CardLayout.
     */
    private void configureScreens() {
        this.mainContainer.add(
            this.homePanel,
            HOME_CARD
        );

        this.mainContainer.add(
            this.gamePanel,
            GAME_CARD
        );

        add(this.mainContainer);

        showHome();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "The returned panel is intentionally exposed through its view interface so" 
                            + "controllers can register listeners and update the UI."
    )
    @Override
    public HomeView getHomeView() {
        return this.homePanel;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "The returned panel is intentionally exposed through its view interface so" 
                            + "controllers can register listeners and update the UI."
    )
    @Override
    public GameView getGameView() {
        return this.gamePanel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showHome() {
        this.cardLayout.show(
            this.mainContainer,
            HOME_CARD
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showGame() {
        this.cardLayout.show(
            this.mainContainer,
            GAME_CARD
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void display() {
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        dispose();
    }
}
