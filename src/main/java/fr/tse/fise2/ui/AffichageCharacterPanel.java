package fr.tse.fise2.ui;

import fr.tse.fise2.api.ApiClient;
import fr.tse.fise2.api.CharacterExtractor;
import fr.tse.fise2.exception.ApiException;
import fr.tse.fise2.model.ComicsCharacter;
import fr.tse.fise2.ui.panels.CharacterGridPanel;

import java.awt.*;
import javax.swing.*;
import java.util.List;

/**
 * Classe AffichageCharacterPanel
 * Cette classe permet de voir le panel de vignettes de personnages.
 * 
 * @author Lauriane TUTALAMO
 */
 public class AffichageCharacterPanel {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Création de l'instance ApiClient
                ApiClient apiClient = new ApiClient();

                // Nom du personnage à rechercher
                String characterName = "super"; // Exemple : "Frankenstein" ou "Spider-Man" ou "Batman" ou "green%20lantern"

                // Appel à la méthode pour récupérer la liste des personnages
                List<ComicsCharacter> characters = CharacterExtractor.getCharacterByName(apiClient, characterName);

                // Affichage dans le terminal pour le suivi
                System.out.println("Liste des personnages récupérés :");
                for (ComicsCharacter character : characters) {
                    System.out.println(character.toString());
                }

                // Créez une fenêtre principale pour afficher le panneau
                JFrame frame = new JFrame("Character Grid Panel");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(1000, 800);
                frame.setLocationRelativeTo(null);

                // Créez le CharacterGridPanel et ajoutez-le à la fenêtre
                CharacterGridPanel gridPanel = new CharacterGridPanel(characters);
                frame.add(gridPanel, BorderLayout.CENTER);

                // Rendre la fenêtre visible
                frame.setVisible(true);

            } catch (ApiException e) {
                JOptionPane.showMessageDialog(null,
                        "Erreur lors de la récupération des informations : " + e.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}