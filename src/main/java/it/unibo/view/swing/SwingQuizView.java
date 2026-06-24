package it.unibo.view.swing;

import it.unibo.view.QuizView;
import it.unibo.view.api.HomeView;
import it.unibo.view.swing.panels.SwingHomePanel;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import java.util.List;
import java.util.function.Consumer;

/**
 * Implementazione Swing della finestra principale del quiz.
 *
 * La finestra contiene due schermate:
 * - HOME: schermata iniziale;
 * - GAME: schermata contenente domanda e risposte.
 *
 * Le schermate vengono gestite attraverso un CardLayout.
 */
public class SwingQuizView extends JFrame implements QuizView {

    private static final int ANSWER_COUNT = 4;

    private static final String HOME_CARD = "HOME";
    private static final String GAME_CARD = "GAME";

    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 700;

    private static final int MINIMUM_WIDTH = 600;
    private static final int MINIMUM_HEIGHT = 600;

    private final CardLayout cardLayout;
    private final JPanel mainContainer;

    private final JLabel progressLabel;
    private final JTextArea questionArea;
    private final JButton[] answerButtons;
    private final JPanel helpPanel;

    private final SwingHomePanel home;

    /**
     * Crea la finestra principale del quiz.
     */
    public SwingQuizView() {
        super("Chi Vuol Essere Ingegnere?");

        this.cardLayout = new CardLayout();
        this.mainContainer = new JPanel(this.cardLayout);

        this.progressLabel = new JLabel();
        this.questionArea = new JTextArea();
        this.answerButtons = new JButton[ANSWER_COUNT];
        this.helpPanel = new JPanel();

        this.home = new SwingHomePanel();

        configureFrame();
        createCards();
    }

    /**
     * Configura le proprietà principali della finestra.
     */
    private void configureFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setMinimumSize(new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT));
    }

    /**
     * Crea e aggiunge le schermate al CardLayout.
     */
    private void createCards() {
        this.mainContainer.add(this.home, HOME_CARD);
        this.mainContainer.add(createGamePanel(), GAME_CARD);

        add(this.mainContainer);

        showHome();
    }

    /**
     * Crea il pannello contenente la schermata di gioco.
     *
     * @return il pannello della schermata di gioco
     */
    private JPanel createGamePanel() {
        final JPanel gamePanel = new JPanel(new BorderLayout(15, 15));

        gamePanel.setBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        gamePanel.add(createTopPanel(), BorderLayout.NORTH);
        gamePanel.add(createHelpPanel(), BorderLayout.WEST);
        gamePanel.add(createQuestionPanel(), BorderLayout.CENTER);
        gamePanel.add(createAnswersPanel(), BorderLayout.SOUTH);

        return gamePanel;
    }

    /**
     * Crea il pannello superiore contenente l'avanzamento del quiz.
     *
     * @return il pannello superiore
     */
    private JPanel createTopPanel() {
        final JPanel topPanel = new JPanel(
            new FlowLayout(FlowLayout.RIGHT)
        );

        topPanel.add(this.progressLabel);

        return topPanel;
    }

    /**
     * Crea il pannello degli aiuti.
     *
     * @return il pannello degli aiuti
     */
    private JPanel createHelpPanel() {
        this.helpPanel.setLayout(
            new BoxLayout(this.helpPanel, BoxLayout.Y_AXIS)
        );

        this.helpPanel.add(new JLabel("Aiuti:"));
        this.helpPanel.add(new JButton("50:50"));
        this.helpPanel.add(Box.createVerticalStrut(10));
        this.helpPanel.add(new JButton("X2"));

        return this.helpPanel;
    }

    /**
     * Crea il pannello contenente il testo della domanda.
     *
     * @return il pannello della domanda
     */
    private JScrollPane createQuestionPanel() {
        this.questionArea.setEditable(false);
        this.questionArea.setFont(
            new Font("Serif", Font.PLAIN, 24)
        );
        this.questionArea.setLineWrap(true);
        this.questionArea.setWrapStyleWord(true);

        return new JScrollPane(this.questionArea);
    }

    /**
     * Crea il pannello contenente i quattro pulsanti delle risposte.
     *
     * @return il pannello delle risposte
     */
    private JPanel createAnswersPanel() {
        final JPanel answersPanel = new JPanel(
            new GridLayout(2, 2, 10, 10)
        );

        for (int i = 0; i < ANSWER_COUNT; i++) {
            this.answerButtons[i] = new JButton();
            answersPanel.add(this.answerButtons[i]);
        }

        return answersPanel;
    }

    /**
     * Restituisce la View della schermata Home.
     *
     * Il Controller può usare questo metodo per collegare gli eventi
     * dei pulsanti presenti nella Home.
     *
     * @return la View della Home
     */
    @Override
    public HomeView getHomeView() {
        return this.home;
    }

    /**
     * Mostra la schermata Home.
     */
    @Override
    public void showHome() {
        this.cardLayout.show(this.mainContainer, HOME_CARD);
    }

    /**
     * Mostra la schermata di gioco.
     */
    @Override
    public void showGame() {
        this.cardLayout.show(this.mainContainer, GAME_CARD);
    }

    /**
     * Collega un listener ai pulsanti delle risposte.
     *
     * @param listener listener che riceve l'indice della risposta selezionata
     */
    @Override
    public void setAnswerListener(final Consumer<Integer> listener) {
        for (int i = 0; i < ANSWER_COUNT; i++) {
            final int answerIndex = i;

            this.answerButtons[i].addActionListener(event -> {
                final int confirmation = JOptionPane.showConfirmDialog(
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
     * Mostra il testo della domanda.
     *
     * @param text testo della domanda
     */
    @Override
    public void setQuestionText(final String text) {
        this.questionArea.setText(text);
        this.questionArea.setCaretPosition(0);
    }

    /**
     * Imposta il testo dei quattro pulsanti delle risposte.
     *
     * @param answers lista delle risposte
     */
    @Override
    public void setAnswers(final List<String> answers) {
        if (answers.size() != ANSWER_COUNT) {
            throw new IllegalArgumentException(
                "La lista deve contenere esattamente "
                    + ANSWER_COUNT
                    + " risposte."
            );
        }

        for (int i = 0; i < ANSWER_COUNT; i++) {
            this.answerButtons[i].setText(answers.get(i));
            this.answerButtons[i].setEnabled(true);
        }
    }

    /**
     * Aggiorna le informazioni sull'avanzamento della partita.
     *
     * @param currentQuestion numero della domanda corrente
     * @param totalQuestions numero totale delle domande
     * @param currentPrize premio corrente
     */
    @Override
    public void updateProgress(
        final int currentQuestion,
        final int totalQuestions,
        final int currentPrize
    ) {
        this.progressLabel.setText(
            "Domanda: "
                + currentQuestion
                + " / "
                + totalQuestions
                + " | Premio: "
                + currentPrize
                + " €"
        );
    }

    /**
     * Disabilita una risposta.
     *
     * @param index indice della risposta da disabilitare
     */
    @Override
    public void disableAnswer(final int index) {
        if (index < 0 || index >= ANSWER_COUNT) {
            throw new IllegalArgumentException(
                "Indice della risposta non valido: " + index
            );
        }

        this.answerButtons[index].setEnabled(false);
    }

    /**
     * Mostra la finestra.
     */
    @Override
    public void display() {
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
public void showCorrectAnswer() {
    JOptionPane.showMessageDialog(
        this,
        "Risposta corretta!",
        "Risultato",
        JOptionPane.INFORMATION_MESSAGE
    );
}

@Override
public void showWrongAnswer() {
    JOptionPane.showMessageDialog(
        this,
        "Risposta errata!",
        "Risultato",
        JOptionPane.ERROR_MESSAGE
    );
}
}