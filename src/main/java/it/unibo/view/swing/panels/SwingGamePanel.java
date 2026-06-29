package it.unibo.view.swing.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import it.unibo.view.api.GameView;
import it.unibo.view.swing.audio.SoundPlayer;

// CHECKSTYLE: MagicNumber OFF
/**
 * Swing implementation of the game screen.
 * 
 * <p>
 * This panel displays the current question, the available answers,
 * the game progress and the help buttons.
 * </p>
 */
public final class SwingGamePanel extends JPanel implements GameView {

    private static final long serialVersionUID = 1L;

    private static final int ANSWER_COUNT = 4;

    private static final int MAIN_HORIZONTAL_GAP = 20;
    private static final int MAIN_VERTICAL_GAP = 20;
    private static final int BORDER_SIZE = 30;

    private static final int QUESTION_FONT_SIZE = 28;
    private static final int ANSWER_FONT_SIZE = 20;
    private static final int PROGRESS_FONT_SIZE = 18;
    private static final int HELP_FONT_SIZE = 16;

    private static final int QUESTION_WIDTH = 600;
    private static final int QUESTION_HEIGHT = 220;

    private static final int ANSWER_BUTTON_WIDTH = 320;
    private static final int ANSWER_BUTTON_HEIGHT = 100;

    private static final int ANSWER_HORIZONTAL_GAP = 20;
    private static final int ANSWER_VERTICAL_GAP = 20;

    private static final int HELP_PANEL_WIDTH = 120;
    private static final int HELP_BUTTON_HEIGHT = 45;

    private final JLabel progressLabel;
    private final JTextArea questionArea;
    private final JButton[] answerButtons;
    private final JButton fiftyFiftyButton;
    private final JButton doubleChanceButton;
    private final JButton switchButton;
    private final SoundPlayer soundPlayer;

    /**
     * Creates the game panel.
     */
    public SwingGamePanel() {
        super(
            new BorderLayout(
                MAIN_HORIZONTAL_GAP,
                MAIN_VERTICAL_GAP
            )
        );

        this.progressLabel = new JLabel();
        this.questionArea = new JTextArea();
        this.answerButtons = new JButton[ANSWER_COUNT];
        this.fiftyFiftyButton = new JButton("50:50");
        this.doubleChanceButton = new JButton("X2");
        this.switchButton = new JButton("Switch");
        this.soundPlayer = new SoundPlayer();

        configurePanel();
        createComponents();
    }

    /**
     * Configures the main game panel.
     */
    private void configurePanel() {
        setBorder(
            BorderFactory.createEmptyBorder(
                BORDER_SIZE,
                BORDER_SIZE,
                BORDER_SIZE,
                BORDER_SIZE
            )
        );
    }

    /**
     * Creates and adds all the game components.
     */
    private void createComponents() {
        add(createProgressPanel(), BorderLayout.NORTH);
        add(createHelpPanel(), BorderLayout.WEST);
        add(createQuestionContainer(), BorderLayout.CENTER);
        add(createAnswersPanel(), BorderLayout.SOUTH);
    }

    /**
     * Creates the upper panel containing the current progress.
     *
     * @return the progress panel
     */
    private JPanel createProgressPanel() {
        final JPanel panel = new JPanel(
            new FlowLayout(FlowLayout.RIGHT)
        );

        this.progressLabel.setFont(
            new Font(
                Font.SANS_SERIF,
                Font.BOLD,
                PROGRESS_FONT_SIZE
            )
        );

        panel.add(this.progressLabel);

        return panel;
    }

    /**
     * Creates the panel containing the help buttons.
     *
     * @return the help panel
     */
    private JPanel createHelpPanel() {
        final JPanel panel = new JPanel();

        panel.setLayout(
            new BoxLayout(panel, BoxLayout.Y_AXIS)
        );

        panel.setPreferredSize(
            new Dimension(HELP_PANEL_WIDTH, 0)
        );

        final JLabel helpLabel = new JLabel(
            "Aiuti",
            SwingConstants.CENTER
        );

        helpLabel.setFont(
            new Font(
                Font.SANS_SERIF,
                Font.BOLD,
                HELP_FONT_SIZE
            )
        );

        helpLabel.setAlignmentX(CENTER_ALIGNMENT);

        panel.add(helpLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(configureHelpButton(this.fiftyFiftyButton));
        panel.add(Box.createVerticalStrut(15));
        panel.add(configureHelpButton(this.doubleChanceButton));
        panel.add(Box.createVerticalStrut(15));
        panel.add(configureHelpButton(this.switchButton));
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    /**
     * Configures a help button.
     *
     * @param button the button to configure
     * @return the configured button
     */
    private JButton configureHelpButton(final JButton button) {
        button.setFont(
            new Font(
                Font.SANS_SERIF,
                Font.BOLD,
                HELP_FONT_SIZE
            )
        );

        button.setAlignmentX(CENTER_ALIGNMENT);

        button.setMaximumSize(
            new Dimension(
                HELP_PANEL_WIDTH,
                HELP_BUTTON_HEIGHT
            )
        );

        button.setPreferredSize(
            new Dimension(
                HELP_PANEL_WIDTH,
                HELP_BUTTON_HEIGHT
            )
        );

        return button;
    }

    /**
     * Creates a container that keeps the question visually centered.
     *
     * @return the question container
     */
    private JPanel createQuestionContainer() {
        final JPanel container = new JPanel(
            new GridBagLayout()
        );

        final GridBagConstraints constraints =
            new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(20, 20, 20, 20);

        container.add(
            createQuestionPanel(),
            constraints
        );

        return container;
    }

    /**
     * Creates the component containing the question text.
     *
     * @return the question scroll pane
     */
    private JScrollPane createQuestionPanel() {
        this.questionArea.setEditable(false);
        this.questionArea.setLineWrap(true);
        this.questionArea.setWrapStyleWord(true);

        this.questionArea.setFont(
            new Font(
                Font.SERIF,
                Font.BOLD,
                QUESTION_FONT_SIZE
            )
        );

        this.questionArea.setMargin(
            new Insets(25, 25, 25, 25)
        );

        this.questionArea.setFocusable(false);

        final JScrollPane scrollPane =
            new JScrollPane(this.questionArea);

        scrollPane.setPreferredSize(
            new Dimension(
                QUESTION_WIDTH,
                QUESTION_HEIGHT
            )
        );

        scrollPane.setMinimumSize(
            new Dimension(
                QUESTION_WIDTH / 2,
                QUESTION_HEIGHT
            )
        );

        scrollPane.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(
                    5,
                    5,
                    5,
                    5
                )
            )
        );

        return scrollPane;
    }

    /**
     * Creates the panel containing the answer buttons.
     *
     * @return the answers panel
     */
    private JPanel createAnswersPanel() {
        final JPanel panel = new JPanel(
            new GridLayout(
                2,
                2,
                ANSWER_HORIZONTAL_GAP,
                ANSWER_VERTICAL_GAP
            )
        );

        panel.setBorder(
            BorderFactory.createEmptyBorder(
                10,
                10,
                10,
                10
            )
        );

        for (int index = 0; index < ANSWER_COUNT; index++) {
            this.answerButtons[index] =
                createAnswerButton();

            panel.add(this.answerButtons[index]);
        }

        return panel;
    }

    /**
     * Creates and configures an answer button.
     *
     * @return the answer button
     */
    private JButton createAnswerButton() {
        final JButton button = new JButton();

        button.setFont(
            new Font(
                Font.SANS_SERIF,
                Font.BOLD,
                ANSWER_FONT_SIZE
            )
        );

        button.setPreferredSize(
            new Dimension(
                ANSWER_BUTTON_WIDTH,
                ANSWER_BUTTON_HEIGHT
            )
        );

        button.setMargin(
            new Insets(15, 20, 15, 20)
        );

        button.setFocusPainted(false);

        return button;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setQuestionText(final String text) {
        this.questionArea.setText(
            Objects.requireNonNull(
                text,
                "The question text cannot be null"
            )
        );

        this.questionArea.setCaretPosition(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAnswers(final List<String> answers) {
        Objects.requireNonNull(
            answers,
            "The answers list cannot be null"
        );

        if (answers.size() != ANSWER_COUNT) {
            throw new IllegalArgumentException(
                "Exactly "
                    + ANSWER_COUNT
                    + " answers are required"
            );
        }

        for (int index = 0; index < ANSWER_COUNT; index++) {
            this.answerButtons[index].setText(
                Objects.requireNonNull(
                    answers.get(index),
                    "An answer text cannot be null"
                )
            );

            this.answerButtons[index].setEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateProgress(
        final int currentQuestionNumber,
        final int totalQuestions,
        final int currentScore
    ) {
        this.progressLabel.setText(
            "Domanda "
                + currentQuestionNumber
                + " / "
                + totalQuestions
                + "  |  Punteggio: "
                + currentScore
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAnswerListener(
        final Consumer<Integer> listener
    ) {
        Objects.requireNonNull(
            listener,
            "The answer listener cannot be null"
        );

        for (int index = 0; index < ANSWER_COUNT; index++) {
            final int answerIndex = index;

            this.answerButtons[index].addActionListener(event -> {
                final int confirmation =
                    JOptionPane.showConfirmDialog(
                        this,
                        "Sei sicuro della risposta?",
                        "Conferma risposta",
                        JOptionPane.YES_NO_OPTION
                    );

                if (confirmation == JOptionPane.YES_OPTION) {
                    listener.accept(answerIndex);
                }
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disableAnswer(final int answerIndex) {
        validateAnswerIndex(answerIndex);
        this.answerButtons[answerIndex].setEnabled(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showCorrectAnswer() {
        this.soundPlayer.play("/sounds/correct.wav");
        JOptionPane.showMessageDialog(
            this,
            "Risposta corretta!",
            "Risultato",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showWrongAnswer() {
        this.soundPlayer.play("/sounds/wrong.wav");
        JOptionPane.showMessageDialog(
            this,
            "Risposta errata!",
            "Risultato",
            JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showGameOver(final int finalScore) {
        this.soundPlayer.play("/sounds/lose.wav");
        JOptionPane.showMessageDialog(
            this,
            "Risposta errata!\n"
                + "La partita è terminata.\n"
                + "Punteggio finale: "
                + finalScore,
            "Game Over",
            JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showGameWon(final int finalScore) {
        this.soundPlayer.play("/sounds/win.wav");
        JOptionPane.showMessageDialog(
            this,
            "Complimenti!\n"
                + "Hai completato il quiz.\n"
                + "Punteggio finale: "
                + finalScore,
            "Vittoria",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Validates an answer index.
     *
     * @param answerIndex the answer index
     */
    private void validateAnswerIndex(final int answerIndex) {
        if (
            answerIndex < 0
                || answerIndex >= ANSWER_COUNT
        ) {
            throw new IllegalArgumentException(
                "Invalid answer index: " + answerIndex
            );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFiftyFiftyListener(final Runnable listener) {
        Objects.requireNonNull(
            listener,
            "The 50:50 listener cannot be null"
        );

        this.fiftyFiftyButton.addActionListener(
            event -> listener.run()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDoubleChanceListener(final Runnable listener) {
        Objects.requireNonNull(
            listener,
            "The Double Chance listener cannot be null"
        );

        this.doubleChanceButton.addActionListener(
            event -> listener.run()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSwitchListener(final Runnable listener) {
        Objects.requireNonNull(
            listener,
            "The Switch listener cannot be null"
        );

        this.switchButton.addActionListener(
            event -> listener.run()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disableFiftyFifty() {
        this.fiftyFiftyButton.setEnabled(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disableDoubleChance() {
        this.doubleChanceButton.setEnabled(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disableSwitch() {
        this.switchButton.setEnabled(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enableAllHelps() {
        this.fiftyFiftyButton.setEnabled(true);
        this.doubleChanceButton.setEnabled(true);
        this.switchButton.setEnabled(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showFiftyFiftyUsed() {
        JOptionPane.showMessageDialog(
            this,
            "Due risposte errate sono state eliminate.",
            "50:50",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showDoubleChanceUsed() {
        JOptionPane.showMessageDialog(
            this,
            "Doppia chance attivata per questa domanda.",
            "Double Chance",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showSwitchUsed() {
        JOptionPane.showMessageDialog(
            this,
            "La domanda è stata sostituita.",
            "Switch",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
