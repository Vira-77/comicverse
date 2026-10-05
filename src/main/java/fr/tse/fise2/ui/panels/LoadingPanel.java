package fr.tse.fise2.ui.panels;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class LoadingPanel extends JPanel {
    private static final int POINTS_COUNT = 3; // Nombre de points animés
    private static final int DIAMETER = 45; // Diamètre des points
    private static final int SPACING = 10; // Espacement entre les points
    private static final int ANIMATION_DELAY = 200; // Délai entre les étapes de l'animation
    private int currentPoint = 0; // Point actif dans l'animation
    private static final String BADGE_IMAGE_PATH = "/images/badge_comicverse.png";

    private JLabel loadingLabel; // Label pour afficher le texte "Loading..."
    private Image badgeImage; // Image pour le badge Comicverse

    public LoadingPanel() {
        setLayout(new GridBagLayout()); // Utilisation de GridBagLayout pour centrer verticalement et horizontalement
        setBackground(Color.WHITE);

        // Créer un panneau pour contenir l'image, le texte et l'animation
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Layout vertical
        contentPanel.setBackground(Color.WHITE);

        // Charger l'image badge_comicverse si elle existe
        try {
            badgeImage = ImageIO.read(getClass().getResource(BADGE_IMAGE_PATH));
        } catch (IOException e) {
            badgeImage = null; // Si l'image n'est pas trouvée, badgeImage restera null
        }

        // Si l'image existe, ajouter un label avec l'image en haut
        if (badgeImage != null) {
            // Redimensionner l'image pour ne pas qu'elle prenne trop de place
            int badgeWidth = 350;  // Largeur de l'image
            int badgeHeight = 350;  // Hauteur de l'image
            Image scaledImage = badgeImage.getScaledInstance(badgeWidth, badgeHeight, Image.SCALE_SMOOTH);
            JLabel badgeLabel = new JLabel(new ImageIcon(scaledImage));
            badgeLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrer l'image
            contentPanel.add(badgeLabel); // Ajouter l'image au-dessus du texte
            contentPanel.add(Box.createRigidArea(new Dimension(0, 4))); // Ajouter un espace après l'image
        }

        // Créer le texte "Loading..."
        loadingLabel = new JLabel("Loading...", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Arial", Font.BOLD, 30));
        loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadingLabel.setForeground(Color.BLACK);

        // Ajouter le texte au panneau
        contentPanel.add(loadingLabel);

        // Ajouter un espace entre le texte et l'animation
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Créer un panneau pour l'animation
        JPanel animationPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int panelWidth = getWidth();
                int totalWidth = (DIAMETER * POINTS_COUNT) + (SPACING * (POINTS_COUNT - 1));
                int startX = (panelWidth - totalWidth) / 2; // Centrer les points horizontalement
                int animationY = getHeight() / 2 - 20; // Centrer les points verticalement

                g.setColor(Color.YELLOW);
                for (int i = 0; i < POINTS_COUNT; i++) {
                    if (i == currentPoint) {
                        g.fillOval(startX + i * (DIAMETER + SPACING), animationY, DIAMETER, DIAMETER);
                    } else {
                        g.drawOval(startX + i * (DIAMETER + SPACING), animationY, DIAMETER, DIAMETER);
                    }
                }
            }
        };
        animationPanel.setPreferredSize(new Dimension(200, 50));
        animationPanel.setBackground(Color.WHITE);
        contentPanel.add(animationPanel);

        // Ajouter le panneau de contenu au centre de la page
        add(contentPanel, new GridBagConstraints());

        // Créer un Timer pour gérer l'animation des points
        Timer timer = new Timer(ANIMATION_DELAY, e -> {
            // Mettre à jour le point actuel pour l'animation
            currentPoint = (currentPoint + 1) % POINTS_COUNT;
            repaint(); // Redessiner l'animation
        });

        // Démarrer l'animation
        timer.start();
    }
}