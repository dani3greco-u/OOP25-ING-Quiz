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
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.unibo.view.api.HomeView;

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
public class SwingHomePanel extends JPanel implements HomeView {

    private static final long serialVersionUID = 1L;

    private final JButton btnInfo;
    private final JButton btnLeaderboard;
    private final JTextField txtName;
    private final JButton btnStart;
    private final JButton btnTraining;
    private final JButton btnExit;

    /**
     * Creates and configures the home screen.
     */
    public SwingHomePanel() {
        this.txtName = new JTextField(15);
        this.txtName.setFont(
            new Font("Arial", Font.PLAIN, 24)
        );

        this.btnStart = new JButton("START");
        this.btnStart.setFont(
            new Font("Arial", Font.BOLD, 20)
        );
        this.btnStart.setPreferredSize(
            new Dimension(180, 50)
        );

        this.btnTraining = new JButton("TRAINING");
        this.btnTraining.setFont(
            new Font("Arial", Font.BOLD, 20)
        );
        this.btnTraining.setPreferredSize(
            new Dimension(180, 50)
        );

        this.btnInfo = createIconButton(
            "/info.png",
            "Info",
            40,
            40
        );

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

        final JPanel topBar = new JPanel(
            new BorderLayout()
        );

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

        topBar.add(
            topRightPanel,
            BorderLayout.EAST
        );

        add(topBar, BorderLayout.NORTH);

        final JPanel centerContent =
            new JPanel(new GridBagLayout());

        final GridBagConstraints constraints =
            new GridBagConstraints();

        constraints.insets =
            new Insets(15, 15, 15, 15);

        constraints.gridx = 0;

        final JLabel logoLabel = new JLabel();
        final URL logoUrl =
            getClass().getResource("/logo.png");

        if (logoUrl != null) {
            final ImageIcon originalLogo =
                new ImageIcon(logoUrl);

            final Image scaledLogo =
                originalLogo
                    .getImage()
                    .getScaledInstance(
                        300,
                        250,
                        Image.SCALE_SMOOTH
                    );

            logoLabel.setIcon(
                new ImageIcon(scaledLogo)
            );
        } else {
            logoLabel.setText(
                "IMMAGINE LOGO MANCANTE"
            );

            logoLabel.setFont(
                new Font(
                    "Arial",
                    Font.BOLD,
                    40
                )
            );
        }

        constraints.weighty = 0.3;
        constraints.anchor =
            GridBagConstraints.SOUTH;
        constraints.gridy = 0;

        centerContent.add(
            logoLabel,
            constraints
        );

        constraints.weighty = 0.1;
        constraints.anchor =
            GridBagConstraints.CENTER;
        constraints.gridy = 1;

        final JLabel nameLabel =
            new JLabel("Inserisci il tuo nome:");

        nameLabel.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                24
            )
        );

        centerContent.add(
            nameLabel,
            constraints
        );

        constraints.weighty = 0.1;
        constraints.gridy = 2;

        centerContent.add(
            this.txtName,
            constraints
        );

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
        constraints.anchor =
            GridBagConstraints.NORTH;
        constraints.gridy = 3;

        centerContent.add(
            buttonsPanel,
            constraints
        );

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

        final URL iconUrl =
            getClass().getResource(imagePath);

        if (iconUrl != null) {
            final ImageIcon originalIcon =
                new ImageIcon(iconUrl);

            final Image scaledImage =
                originalIcon
                    .getImage()
                    .getScaledInstance(
                        width,
                        height,
                        Image.SCALE_SMOOTH
                    );

            button.setIcon(
                new ImageIcon(scaledImage)
            );

            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setOpaque(false);
            button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
            );
        } else {
            button.setText(fallbackText);
        }

        return button;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnInfo(final Runnable listener) {
        this.btnInfo.addActionListener(
            event -> listener.run()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnStart(
        final Consumer<String> listener
    ) {
        this.btnStart.addActionListener(event -> {
            final String name =
                this.txtName.getText().trim();

            if (!name.isEmpty()) {
                listener.accept(name);
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Devi inserire un nome per iniziare!"
                );
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnLeaderboard(
        final Runnable listener
    ) {
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
    public void setOnTraining(
        final Consumer<String> listener
    ) {
        this.btnTraining.addActionListener(event -> {
            final String name =
                this.txtName.getText().trim();

            if (!name.isEmpty()) {
                listener.accept(name);
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Devi inserire un nome per l'allenamento!"
                );
            }
        });
    }
}