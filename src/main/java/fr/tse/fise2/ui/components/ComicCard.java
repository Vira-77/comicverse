package fr.tse.fise2.ui.components;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import fr.tse.fise2.api.ApiClient;
import fr.tse.fise2.api.ComicExtractor;
import fr.tse.fise2.model.Comic;
import fr.tse.fise2.ui.ComicsInformation;
import fr.tse.fise2.ui.LoginFrame;
import fr.tse.fise2.ui.components.ComicButton.ButtonType;
import fr.tse.fise2.ui.panels.MainPanel;

/**
 * Classe ComicTile
 * Cette classe permet de créer des vignettes pour les comics.
 * Elle peut servir de modèle pour toutes les vignettes à afficher.
 * 
 * @autor Lauriane TUTALAMO
 */
 public class ComicCard extends JPanel  {
    private final Comic comic;
    private final JButton nameButton;
    private final JPanel buttonsPanel;
    private final JButton imageButton;
    private ComicButton boutonFavoris;
    private ComicButton boutonBibliotheque;
    private ComicButton boutonAchat;

    public ComicCard(Comic comic) {
        this.comic = comic;
        this.nameButton =  new JButton(comic.getName());
        this.buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        this.buttonsPanel.setBackground(Color.BLACK);

        boolean userConnected = LoginFrame.isUserLoggedIn(); // Vérifier si un utilisateur est connecté
        int btnWidth=30;
        int btnHeight=30;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // Bordure pour chaque vignette

        // Partie centrale : Image du comic
        imageButton = new JButton();
        try {
            if (comic.getImageUrl() != null) {
                ImageIcon comicImage = new ImageIcon(new URL(comic.getImageUrl()));
                Image scaledImage = comicImage.getImage().getScaledInstance(200, 350, Image.SCALE_SMOOTH);
                imageButton.setIcon(new ImageIcon(scaledImage));
            } else {
                throw new Exception("Image manquante");
            }
        } catch (Exception e) {
            imageButton.setBackground(Color.BLACK); // Image noire par défaut
            imageButton.setText("No Image");
        }
        imageButton.setFocusPainted(false); // Désactiver le focus visuel pour un rendu propre
        imageButton.addActionListener(e -> {
            System.out.println("Opening comic details for: " + comic.getName()+" id="+ comic.getId());
            try {
                ApiClient apiClient = new ApiClient();
                Comic comicCompleted = ComicExtractor.getComicByID(apiClient, Integer.toString(comic.getId()));

                // Use comicCompleted directly without setting it to the existing comic
                JPanel contentPanel = MainPanel.getContentPanel();
                CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
                String constraint = ComicsInformation.class.getSimpleName();
                ComicsInformation comicsInformation = new ComicsInformation(comicCompleted);
                contentPanel.add(constraint, comicsInformation);
                cardLayout.show(contentPanel, constraint);
                comicsInformation.getBackButton().addActionListener(e1 ->
                    cardLayout.show(contentPanel, MainPanel.getCurrentPanel())
                );
            } catch (Exception ex) {
                ex.printStackTrace();
                // Optional: Show a dialog or log a message for the user
            }
        });
        add(imageButton, BorderLayout.CENTER);

        // Partie inférieure : Nom du comic et section en deux colonnes
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(2, 1, 5, 5)); // Deux lignes : Nom (1ère ligne), Section (2e ligne)

        // Ligne 1 : Nom du comic
        this.nameButton.setHorizontalAlignment(SwingConstants.CENTER); // Centrer le texte
        this.nameButton.setFocusPainted(false);
        this.nameButton.addActionListener(e -> {
            System.out.println("Opening comic details for: " + comic.getName()+" id="+ comic.getId());
            System.out.println("Description: "+ comic.getDescription());
        });
        this.nameButton.setToolTipText(comic.getName()); 
        bottomPanel.add(this.nameButton); // Ajouter le nom à la première ligne

        // Ligne 2 : Divisée en deux colonnes (tag et boutons)
        JPanel tagAndButtonsPanel = new JPanel(new GridLayout(1, 2, 10, 0)); // Une ligne, deux colonnes
        tagAndButtonsPanel.setBackground(Color.BLACK); 

        // Colonne 1 : Tag "Comic"
        JPanel tagPanel = new JPanel();
        tagPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Disposition de gauche pour l'icône
        tagPanel.setBackground(Color.BLACK); // Couleur de fond pour différencier le tag

        // Ajouter l'icône du tag
        JLabel tagIcon = new JLabel();
        try {
            URL tagImageUrl = getClass().getClassLoader().getResource("images/tagcomic.jpg");
            if (tagImageUrl != null) {
                ImageIcon tagImage = new ImageIcon(tagImageUrl);
                Image scaledTagImage = tagImage.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                tagIcon.setIcon(new ImageIcon(scaledTagImage));
            } else {
                tagIcon.setText("Image manquante");
            }
        } catch (Exception e) {
            tagIcon.setText("Image manquante");
        }

        JLabel tagLabel = new JLabel("Comic");
        tagLabel.setForeground(Color.white);
        tagLabel.setFont(new Font("Arial", Font.BOLD, 16));
        tagPanel.add(tagIcon);
        tagPanel.add(tagLabel);

        tagAndButtonsPanel.add(tagPanel); // Ajouter le tag dans la première colonne

        // Colonne 2 : Boutons alignés horizontalement
        this.buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5)); // Disposition horizontale pour les boutons
        this.buttonsPanel.setBackground(Color.BLACK);

        // Si l'utilisateur est connecté, afficher les boutons
        if (userConnected) {
            // Bouton "Favoris" (F)
            boutonFavoris = new ComicButton(comic, ButtonType.FAVORITE);
            boutonFavoris.setPreferredSize(new Dimension(btnWidth, btnHeight)); // Taille ajustée
            boutonFavoris.setToolTipText("Ajouter aux Favoris");
            buttonsPanel.add(boutonFavoris);
            

            // Bouton "Bibliothèque" (B)
            boutonBibliotheque = new ComicButton(comic, ButtonType.LIBRARY);
            boutonBibliotheque.setPreferredSize(new Dimension(btnWidth, btnHeight)); // Taille ajustée
            boutonBibliotheque.setToolTipText("Ajouter à la Bibliothèque");
            buttonsPanel.add(boutonBibliotheque);

            // Bouton "Achat" (A)
            boutonAchat = new ComicButton(comic, ButtonType.WISHLIST);
            boutonAchat.setPreferredSize(new Dimension(btnWidth, btnHeight)); // Taille ajustée
            boutonAchat.setToolTipText("Ajouter à la Liste d'Achats");
            buttonsPanel.add(boutonAchat);
        }

        tagAndButtonsPanel.add(this.buttonsPanel); // Ajouter les boutons dans la deuxième colonne

        // Ajouter la deuxième ligne au panneau inférieur
        bottomPanel.add(tagAndButtonsPanel);

        // Ajouter le panneau inférieur à la vignette
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Getters pour les tests unitaires
    public Comic getComic() {
        return comic;
    }


    public JButton getNameButton() {
        return nameButton;
    }

    public JButton getImageButton() {
        return imageButton;
    }

    public ComicButton getBoutonFavoris() {
        return boutonFavoris;
    }

    public ComicButton getBoutonBibliotheque() {
        return boutonBibliotheque;
    }

    public ComicButton getBoutonAchat() {
        return boutonAchat;
    }

    // Vérifier si la vignette contient un panneau de boutons
    public boolean hasButtonsPanel() {
        return buttonsPanel != null && buttonsPanel.getComponentCount() > 0;
    }

    public boolean hasUserButtons() {
        return buttonsPanel.getComponentCount() > 0; 
    }

    public JPanel getButtonsPanel(){
        return buttonsPanel;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ComicTile [");
        sb.append("Comic Name: ").append(comic.getName()).append(", ");
        sb.append("Has Image: ").append(imageButton.getIcon() != null ? "Yes" : "No").append(", ");
        
        // Détails sur les boutons
        sb.append("Buttons Panel: ");
        if (buttonsPanel != null && buttonsPanel.getComponentCount() > 0) {
            sb.append("Contains ").append(buttonsPanel.getComponentCount()).append(" button(s): ");
            for (Component comp : buttonsPanel.getComponents()) {
                if (comp instanceof ComicButton) {
                    ComicButton button = (ComicButton) comp;
                    sb.append("[").append(button.getType().getLabel());
                    
                    // Si l'image est présente, ajouter l'info de l'image
                    ImageIcon icon = (ImageIcon) button.getIcon();
                    if (icon != null) {
                        sb.append(", Icon: ").append(icon.getDescription());
                    } else {
                        sb.append(", No Image");
                    }
                    
                    sb.append("] ");
                }
            }
        } else {
            sb.append("Empty");
        }
        
        sb.append("]");
        return sb.toString();
    }

    
}