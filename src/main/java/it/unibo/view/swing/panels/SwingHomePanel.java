package it.unibo.view.swing.panels;

import javax.swing.*;
import it.unibo.view.api.HomeView;
import java.awt.*;
import java.net.URL;
import java.util.function.Consumer;

/**
 * Swing Panel that implements the HomeView interface. This panel is responsible for displaying the initial screen
 * where the user can enter their name and start the game. It includes buttons for starting the game, accessing the leaderboard, 
 * viewing information, and exiting the application.
 */
public class SwingHomePanel extends JPanel implements HomeView {
    private final JButton btnInfo;
    private final JButton btnLeaderboard; 
    private final JTextField txtName;
    private final JButton btnStart;
    private final JButton btnTraining;
    private final JButton btnExit;

    public SwingHomePanel() {

        // 2. Inizializzazione e ingrandimento font/dimensioni
        this.txtName = new JTextField(15);
        this.txtName.setFont(new Font("Arial", Font.PLAIN, 24));
        
        this.btnStart = new JButton("START");
        this.btnStart.setFont(new Font("Arial", Font.BOLD, 20));
        this.btnStart.setPreferredSize(new Dimension(180, 50));
        
        this.btnTraining = new JButton("TRAINING");
        this.btnTraining.setFont(new Font("Arial", Font.BOLD, 20));
        this.btnTraining.setPreferredSize(new Dimension(180, 50));

        this.btnInfo = createIconButton("/info.png", "Info", 40, 40);
        this.btnLeaderboard = createIconButton("/trophy.png", "Leaderboard", 40, 40);
        this.btnExit = createIconButton("/exit.png", "Exit", 40, 40);

        //nord
        this.setLayout(new BorderLayout());
        JPanel topBar = new JPanel(new BorderLayout());
        // padding 
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        topBar.add(btnInfo, BorderLayout.WEST);
        
        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        topRightPanel.add(btnLeaderboard);
        topRightPanel.add(btnExit);
        topBar.add(topRightPanel, BorderLayout.EAST);
        
        this.add(topBar, BorderLayout.NORTH);

        //center
        JPanel centerContent = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0; 

        JLabel logoLabel = new JLabel();
        URL logoUrl = getClass().getResource("/logo.png");
        if (logoUrl != null) {
            ImageIcon originalLogo = new ImageIcon(logoUrl);
            // Cambia 300 e 150 se lo vuoi più grande o più piccolo!
            Image scaledLogo = originalLogo.getImage().getScaledInstance(300, 250, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledLogo));
        } else {
            logoLabel.setText("IMMAGINE LOGO MANCANTE"); 
            logoLabel.setFont(new Font("Arial", Font.BOLD, 40));
        }
        gbc.weighty = 0.3; // Responsive: distribuisce lo spazio vuoto in alto
        gbc.anchor = GridBagConstraints.SOUTH; // Spinge verso il basso
        gbc.gridy = 0;
        centerContent.add(logoLabel, gbc);

        // B) Testo "Inserisci nome"
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridy = 1;
        JLabel lblNome = new JLabel("Inserisci il tuo nome:");
        lblNome.setFont(new Font("Arial", Font.BOLD, 24));
        centerContent.add(lblNome, gbc);

        // C) Campo di testo
        gbc.weighty = 0.1;
        gbc.gridy = 2;
        centerContent.add(txtName, gbc);

        // D) I due bottoni Start e Training 
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        buttonsPanel.add(btnStart);
        buttonsPanel.add(btnTraining);

        gbc.weighty = 0.5; // Responsive: distribuisce lo spazio vuoto in basso
        gbc.anchor = GridBagConstraints.NORTH; // Spinge verso l'alto
        gbc.gridy = 3;
        centerContent.add(buttonsPanel, gbc);

        // Aggiungiamo tutto il blocco centrale
        this.add(centerContent, BorderLayout.CENTER);
    }

    // ==========================================================
    // UTILITY METHOD: Crea e ridimensiona i bottoni con le icone
    // ==========================================================
    private JButton createIconButton(String imagePath, String fallbackText, int width, int height) {
        JButton button = new JButton();
        URL iconUrl = getClass().getResource(imagePath);
        
        if (iconUrl != null) {
            ImageIcon originalIcon = new ImageIcon(iconUrl);
            Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
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

    // ==========================================================
    // IMPLEMENTAZIONE INTERFACCIA (I "cavi" per il Controller)
    // ==========================================================
    @Override
    public void setOnInfo(Runnable listener) {
        this.btnInfo.addActionListener(e -> listener.run());
    }

    @Override
    public void setOnStart(Consumer<String> listener) {
        this.btnStart.addActionListener(e -> {
            String name = txtName.getText().trim();
            if (!name.isEmpty()) {
                listener.accept(name);
            } else {
                JOptionPane.showMessageDialog(this, "Devi inserire un nome per iniziare!");
            }
        });
    }

    @Override
    public void setOnLeaderboard(Runnable listener) {
        this.btnLeaderboard.addActionListener(e -> listener.run());
    }

    @Override
    public void setOnExit(Runnable listener) {
        this.btnExit.addActionListener(e -> listener.run());
    }

    @Override
    public void setOnTraining(Consumer<String> listener) {
        this.btnTraining.addActionListener(e -> {
            String name = txtName.getText().trim();
            if (!name.isEmpty()) {
                listener.accept(name); 
            } else {
                JOptionPane.showMessageDialog(this, "Devi inserire un nome per l'allenamento!");
            }
        });
    }
}