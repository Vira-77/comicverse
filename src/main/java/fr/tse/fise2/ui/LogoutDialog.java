package fr.tse.fise2.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Fenêtre de déconnexion 
 * Cette classe gère l'interface de confirmation de déconnexion
 * 
 * @version 1.0
 * @author Samuel PLET
 */
public class LogoutDialog extends JDialog {

    private static final String LOGOUT_ICON_PATH = "/images/logout_icon.png";
    private static final String FONT_PATH = "/fonts/Heroes Legend.ttf";
    private static final String BACKGROUND_IMAGE_PATH = "/images/login_background.png";
    private static final Dimension WINDOW_SIZE = new Dimension(600, 400);
    private boolean confirmed = false;

    public LogoutDialog(JFrame parent) {
        super(parent, "Confirm Logout", true);
        
        // Configuration de base
        setSize(WINDOW_SIZE);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout());

        // Création du panel de fond
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());

        // Création du panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setOpaque(false);

        // Icône de déconnexion
        try {
            ImageIcon logoutIcon = new ImageIcon(getClass().getResource(LOGOUT_ICON_PATH));
            Image scaledImage = logoutIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainPanel.add(iconLabel);
            mainPanel.add(Box.createVerticalStrut(30));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Message de confirmation
        JLabel messageLabel = new JLabel("Are you sure you want to logout?");
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setBorder(new EmptyBorder(5, 0, 5, 0));
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, 
                getClass().getResourceAsStream(FONT_PATH)).deriveFont(12f);
            messageLabel.setFont(customFont);
        } catch (Exception e) {
            messageLabel.setFont(new Font("Arial", Font.BOLD, 12));
        }
        mainPanel.add(messageLabel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        JButton yesButton = createStyledButton("Yes", Color.RED);
        JButton noButton = createStyledButton("No", new Color(30, 30, 30));

        yesButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });
        noButton.addActionListener(e -> dispose());

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        mainPanel.add(buttonPanel);

        backgroundPanel.add(mainPanel);
        setContentPane(backgroundPanel);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 40));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // Panel de fond avec image
    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource(BACKGROUND_IMAGE_PATH));
                backgroundImage = icon.getImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
            } else {
                g.setColor(Color.GRAY);
                g.fillRect(0, 0, this.getWidth(), this.getHeight());
            }
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}