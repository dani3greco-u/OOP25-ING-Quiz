package it.unibo.view.swing.panels;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import it.unibo.model.data.leaderboard.LeaderboardEntry;
import it.unibo.view.api.HomeView;

//CHECKSTYLE: MultipleStringLiterals OFF
/**
 * Swing implementation of the application home screen.
 *
 * <p>
 * This panel allows the user to enter a player name and choose
 * one of the available actions, such as starting a new game,
 * starting the training mode, opening the leaderboard,
 * displaying application information or exiting the application.
 * </p>
 *
 * <p>
 * User interactions are exposed through the methods defined
 * by {@link HomeView}. The panel does not execute application
 * logic directly, but delegates actions to the registered listeners.
 * </p>
 */
public final class SwingHomePanel extends JPanel implements HomeView {

    public static final long serialVersionUID = 1L;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final String DEFAULT_FONT = "Arial";

    private final JButton btnInfo;
    private final JButton btnLeaderboard;
    private final JTextField txtName;
    private final JButton btnStart;
    private final JButton btnTraining;
    private final JButton btnExit;

    // CHECKSTYLE: MagicNumber OFF
    /**
     * Creates and configures the home screen.
     */
    public SwingHomePanel() {
        this.txtName = new JTextField(15);
        this.txtName.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 24));

        this.btnStart = new JButton("START");
        this.btnStart.setFont(new Font(DEFAULT_FONT, Font.BOLD, 20));
        this.btnStart.setPreferredSize(new Dimension(180, 50));

        this.btnTraining = new JButton("TRAINING");
        this.btnTraining.setFont(new Font(DEFAULT_FONT, Font.BOLD, 20));
        this.btnTraining.setPreferredSize(new Dimension(180, 50));

        this.btnInfo = createIconButton(
            "/info.png",
            "Info",
            40,
            40
        );

        this.btnInfo.addActionListener(event -> {
            JOptionPane.showMessageDialog(
                this,
                """
                Welcome to the ING Quiz!

                Objective:
                Answer 15 questions correctly to win.

                Available Lifelines:

                50:50: removes two incorrect answers.
                Double Chance: gives you a second attempt.
                Switch: replaces the current question.

                """,
                "Information",
                JOptionPane.INFORMATION_MESSAGE
            );
        });

        this.btnLeaderboard = createIconButton(
            "/trophy.png",
            "Leaderboard",
            40,
            40
        );

        this.btnExit = createIconButton(
            "/exit.png",
            "Exit",
            40,
            40
        );

        setLayout(new BorderLayout());

        final JPanel topBar = new JPanel(new BorderLayout());

        topBar.setBorder(
            BorderFactory.createEmptyBorder(
                10,
                10,
                10,
                10
            )
        );

        topBar.add(this.btnInfo, BorderLayout.WEST);

        final JPanel topRightPanel = new JPanel(
            new FlowLayout(
                FlowLayout.RIGHT,
                10,
                0
            )
        );

        topRightPanel.add(this.btnLeaderboard);
        topRightPanel.add(this.btnExit);

        topBar.add(topRightPanel, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        final JPanel centerContent = new JPanel(new GridBagLayout());

        final GridBagConstraints constraints = new GridBagConstraints();

        constraints.insets = new Insets(15, 15, 15, 15);

        constraints.gridx = 0;

        final JLabel logoLabel = new JLabel();
        final URL logoUrl = getClass().getResource("/logo.png");

        if (logoUrl != null) {
            final ImageIcon originalLogo = new ImageIcon(logoUrl);

            final Image scaledLogo =
                originalLogo
                    .getImage()
                    .getScaledInstance(
                        300,
                        250,
                        Image.SCALE_SMOOTH
                    );

            logoLabel.setIcon(new ImageIcon(scaledLogo));
        } else {
            logoLabel.setText("Image not found");

            logoLabel.setFont(
                new Font(
                    DEFAULT_FONT,
                    Font.BOLD,
                    40
                )
            );
        }

        constraints.weighty = 0.3;
        constraints.anchor = GridBagConstraints.SOUTH;
        constraints.gridy = 0;

        centerContent.add(logoLabel, constraints);

        constraints.weighty = 0.1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridy = 1;

        final JLabel nameLabel = new JLabel("Enter your name:");

        nameLabel.setFont(
            new Font(
                DEFAULT_FONT,
                Font.BOLD,
                24
            )
        );

        centerContent.add(nameLabel, constraints);

        constraints.weighty = 0.1;
        constraints.gridy = 2;

        centerContent.add(this.txtName, constraints);

        final JPanel buttonsPanel =
            new JPanel(
                new FlowLayout(
                    FlowLayout.CENTER,
                    30,
                    0
                )
            );

        buttonsPanel.add(this.btnStart);
        buttonsPanel.add(this.btnTraining);

        constraints.weighty = 0.5;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.gridy = 3;

        centerContent.add(buttonsPanel, constraints);

        add(centerContent, BorderLayout.CENTER);
    }

    /**
     * Creates a button containing a scaled icon.
     *
     * <p>
     * If the requested image resource cannot be found,
     * the button displays the provided fallback text.
     * </p>
     *
     * @param imagePath the classpath path of the icon resource
     * @param fallbackText the text displayed when the icon is unavailable
     * @param width the icon width in pixels
     * @param height the icon height in pixels
     * @return the configured button
     */
    private JButton createIconButton(
        final String imagePath,
        final String fallbackText,
        final int width,
        final int height
    ) {
        final JButton button = new JButton();

        final URL iconUrl = getClass().getResource(imagePath);

        if (iconUrl != null) {
            final ImageIcon originalIcon = new ImageIcon(iconUrl);

            final Image scaledImage =
                originalIcon
                    .getImage()
                    .getScaledInstance(
                        width,
                        height,
                        Image.SCALE_SMOOTH
                    );

            button.setIcon(new ImageIcon(scaledImage));

            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setOpaque(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            button.setText(fallbackText);
        }

        return button;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnStart(final Consumer<String> listener) {
        this.btnStart.addActionListener(event -> {
            final String name = this.txtName.getText().trim();

            if (!name.isEmpty()) {
                listener.accept(name);
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "You must enter a name to start!"
                );
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnLeaderboard(final Runnable listener) {
        this.btnLeaderboard.addActionListener(
            event -> listener.run()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnExit(final Runnable listener) {
        this.btnExit.addActionListener(
            event -> listener.run()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnTraining(final Consumer<String> listener) {
        this.btnTraining.addActionListener(event -> {
            final String name = this.txtName.getText().trim();

            if (!name.isEmpty()) {
                listener.accept(name);
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "You must enter a name for training!"
                );
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showLeaderboard(final List<LeaderboardEntry> entries) {
        if (entries.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "The leaderboard is still empty.",
                "Leaderboard",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        final String[] columnNames = {
            "Position",
            "Player",
            "Score",
            "Date",
        };

        final Object[][] rowData = new Object[entries.size()][columnNames.length];

        for (int index = 0; index < entries.size(); index++) {
            final LeaderboardEntry entry = entries.get(index);

            rowData[index][0] = index + 1;
            rowData[index][1] = entry.playerName();
            rowData[index][2] = entry.score();
            rowData[index][3] = entry.achievedAt().format(DATE_FORMATTER);
        }

        final DefaultTableModel tableModel =
            new DefaultTableModel(rowData, columnNames) {

                private static final long serialVersionUID = 1L;

                @Override
                public boolean isCellEditable(final int row, final int column) {
                    return false;
                }
            };

        final JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowSelectionAllowed(false);

        final JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setPreferredSize(new Dimension(520, 280));

        JOptionPane.showMessageDialog(
            this,
            scrollPane,
            "Leaderboard",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
