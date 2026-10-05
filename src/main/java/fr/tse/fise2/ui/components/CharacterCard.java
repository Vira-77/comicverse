package fr.tse.fise2.ui.components;

import javax.swing.*;

import fr.tse.fise2.api.ApiClient;
import fr.tse.fise2.api.CharacterExtractor;
import fr.tse.fise2.model.ComicsCharacter;
import fr.tse.fise2.ui.CharactersInformation;
import fr.tse.fise2.ui.panels.MainPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import fr.tse.fise2.exception.ApiException;
/**
 * Classe CharacterCard
 * Cette classe permet de créer des vignettes pour les personnages.
 * Elle peut servir de modèle pour toutes les vignettes à afficher.
 * 
 * @author Lauriane TUTALAMO
 */
public class CharacterCard extends JPanel {
    public CharacterCard(ComicsCharacter character) {

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // Bordure pour chaque vignette

        // Partie centrale : Image
        JButton imageButton = new JButton();
        try {
            if (character.getPhotoUrl() != null) {
                ImageIcon characterImage = new ImageIcon(new URL(character.getPhotoUrl()));
                Image scaledImage = characterImage.getImage().getScaledInstance(200, 350, Image.SCALE_SMOOTH);
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
            System.out.println("Opening comic details for: " + character.getHeroName()+" id="+ character.getId());
            JPanel contentPanel = MainPanel.getContentPanel();
            CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
            String constraint = CharactersInformation.class.getSimpleName();
            try {
                CharactersInformation charactersInformation = new CharactersInformation(character);
                contentPanel.add(constraint, charactersInformation);
                cardLayout.show(contentPanel, constraint);
                charactersInformation.getBackButton().addActionListener(e1 -> cardLayout.show(contentPanel, MainPanel.getCurrentPanel()));
            } catch (ApiException ex) {
                JOptionPane.showMessageDialog(this, "Error loading character information: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        imageButton.setToolTipText(character.getShortDescription()); 
        add(imageButton, BorderLayout.CENTER);

        // Partie inférieure : Nom et tag dans une grille
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(2, 1)); // 2 lignes : Nom + Tag

        // Ligne 1 : Nom du héros
        JButton nameButton = new JButton(character.getHeroName());
        nameButton.setHorizontalAlignment(SwingConstants.CENTER); // Centrer le texte
        nameButton.setFocusPainted(false);
        //nameButton.setBackground(Color.RED);
        nameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Opening character details for: " + character.getHeroName());
                System.out.println(character.toString());
               
                System.out.println(character.getIssueCredits());
            }
        });
        nameButton.setToolTipText(character.getHeroName()); 
        bottomPanel.add(nameButton);

        // Ligne 2 : Tag "Personnage"
        JPanel tagPanel = new JPanel();
        tagPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Disposition de gauche pour l'icône
        tagPanel.setBackground(Color.BLACK); // Couleur de fond pour différencier le tag

        // Charger l'image du tag à partir du classpath
        JLabel tagIcon = new JLabel();
        try {
            // Charger l'icône du tag depuis le classpath
            URL tagImageUrl = getClass().getClassLoader().getResource("images/tagcharacter.jpg");
            if (tagImageUrl != null) {
                ImageIcon tagImage = new ImageIcon(tagImageUrl);
                // Redimensionner l'image du tag
                Image scaledTagImage = tagImage.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH); // Ajustez la taille ici
                tagIcon.setIcon(new ImageIcon(scaledTagImage)); // Affecter l'image redimensionnée
            } else {
                tagIcon.setText("Image manquante"); // Si l'image n'est pas trouvée
            }
        } catch (Exception e) {
            tagIcon.setText("Image manquante"); // Si l'image n'est pas trouvée
        }

        // Ajouter l'icône et le label "Character" dans le panneau
        JLabel tagLabel = new JLabel("Character");
        tagLabel.setForeground(Color.white);
        tagPanel.add(tagIcon);
        tagPanel.add(tagLabel);
        bottomPanel.add(tagPanel);

        // Ajouter le panneau inférieur à SOUTH
        add(bottomPanel, BorderLayout.SOUTH);
    }


    // Méthode main pour tester la classe et voir la différence entre la méthode getCharacterInfo et getDetailById de la classe CharacterExtractor
    // Normalement, les vignettes sont les mêmes mais les détails sont différents. 
    // Cliquer sur le nom du personnage dans une vignette doit afficher les détails du personnage dans la console
    //Ceux de gauches sont les informations de base (getCharacterByName) et ceux de droite sont les informations détaillées (getCharacterById)
   
    // TODO: Main à supprimer une fois que la personne qui code le même fonctionnement de CharacterExtractor dans ComicExtractor aura terminé
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Character Viewer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLayout(new BorderLayout());

            // Conteneur principal avec deux panneaux pour comparer les deux listes
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new GridLayout(1, 2, 10, 10)); // Deux colonnes
            frame.add(mainPanel, BorderLayout.CENTER);

            try {

                // Création de l'instance ApiClient
                ApiClient apiClient = new ApiClient();

                // Étape 1 : Obtenir une liste limitée à 6 personnages avec getCharacterInfo
                List<ComicsCharacter> charactersFromInfo = CharacterExtractor.getCharacterByName(apiClient,"Spider-Man");

                // Étape 2 : Utiliser getDetailById pour récupérer les détails de chaque personnage
                List<ComicsCharacter> charactersFromDetails = new ArrayList<>();
                for (ComicsCharacter character : charactersFromInfo) {
                    ComicsCharacter detailedCharacter = CharacterExtractor.getCharacterById(apiClient,character.getId());
                    charactersFromDetails.add(detailedCharacter);
                }

                // Panneau pour afficher la première liste
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new GridLayout(0, 1, 10, 10)); // Affichage vertical
                infoPanel.setBorder(BorderFactory.createTitledBorder("From getCharacterInfo"));
                for (ComicsCharacter character : charactersFromInfo) {
                    infoPanel.add(new CharacterCard(character));
                }
                mainPanel.add(new JScrollPane(infoPanel));

                // Panneau pour afficher la seconde liste
                JPanel detailsPanel = new JPanel();
                detailsPanel.setLayout(new GridLayout(0, 1, 10, 10)); // Affichage vertical
                detailsPanel.setBorder(BorderFactory.createTitledBorder("From getDetailById"));
                for (ComicsCharacter character : charactersFromDetails) {
                    detailsPanel.add(new CharacterCard(character));
                }
                mainPanel.add(new JScrollPane(detailsPanel));

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Erreur : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }

            frame.setVisible(true);
        });
    }
}
