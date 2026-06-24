package it.unibo.view.swing.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
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

import it.unibo.view.api.GameView;

/**
 * Swing implementation of the game screen.
 *
 * This panel displays the current question, the available answers,
 * the game progress and the lifeline buttons.
 */
public final class SwingGamePanel extends JPanel implements GameView {

    private static final long serialVersionUID = 1L;

    private static final int ANSWER_COUNT = 4;
    private static final int HORIZONTAL_GAP = 15;
    private static final int VERTICAL_GAP = 15;
    private static final int ANSWER_GAP = 10;
    private static final int BORDER_SIZE = 20;
    private static final int QUESTION_FONT_SIZE = 24;

    private final JLabel progressLabel;
    private final JTextArea questionArea;
    private final JButton[] answerButtons;
    private final JPanel helpPanel;

    /**
     * Creates the game panel.
     */
    public SwingGamePanel() {
        super(new BorderLayout(HORIZONTAL_GAP, VERTICAL_GAP));

        this.progressLabel = new JLabel();
        this.questionArea = new JTextArea();
        this.answerButtons = new JButton[ANSWER_COUNT];
        this.helpPanel = new JPanel();

        configurePanel();
        createComponents();
    }

    /**
     * Configures the main panel.
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
     * Creates and adds all the components of the game screen.
     */
    private void createComponents() {
        add(createProgressPanel(), BorderLayout.NORTH);
        add(createHelpPanel(), BorderLayout.WEST);
        add(createQuestionPanel(), BorderLayout.CENTER);
        add(createAnswersPanel(), BorderLayout.SOUTH);
    }

    /**
     * Creates the panel containing the game progress.
     *
     * @return the progress panel
     */
    private JPanel createProgressPanel() {
        final JPanel panel = new JPanel(
            new FlowLayout(FlowLayout.RIGHT)
        );

        panel.add(this.progressLabel);

        return panel;
    }

    /**
     * Creates the panel containing the lifeline buttons.
     *
     * The listeners of these buttons will be added later,
     * when the lifelines are connected to the controller.
     *
     * @return the lifeline panel
     */
    private JPanel createHelpPanel() {
        this.helpPanel.setLayout(
            new BoxLayout(this.helpPanel, BoxLayout.Y_AXIS)
        );

        this.helpPanel.add(new JLabel("Aiuti:"));
        this.helpPanel.add(new JButton("50:50"));
        this.helpPanel.add(Box.createVerticalStrut(ANSWER_GAP));
        this.helpPanel.add(new JButton("X2"));

        return this.helpPanel;
    }

    /**
     * Creates the scroll pane containing the question text.
     *
     * @return the question scroll pane
     */
    private JScrollPane createQuestionPanel() {
        this.questionArea.setEditable(false);
        this.questionArea.setLineWrap(true);
        this.questionArea.setWrapStyleWord(true);
        this.questionArea.setFont(
            new Font("Serif", Font.PLAIN, QUESTION_FONT_SIZE)
        );

        return new JScrollPane(this.questionArea);
    }

    /**
     * Creates the panel containing the answer buttons.
     *
     * @return the answers panel
     */
    private JPanel createAnswersPanel() {
        final JPanel panel = new JPanel(
            new GridLayout(2, 2, ANSWER_GAP, ANSWER_GAP)
        );

        for (int index = 0; index < ANSWER_COUNT; index++) {
            this.answerButtons[index] = new JButton();
            panel.add(this.answerButtons[index]);
        }

        return panel;
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
                "Exactly " + ANSWER_COUNT + " answers are required"
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
            "Domanda: "
                + currentQuestionNumber
                + " / "
                + totalQuestions
                + " | Punteggio: "
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
                        "Conferma",
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
}