package fr.tse.fise2.ui;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.*;

import java.awt.event.*;

import java.util.Arrays;

import fr.tse.fise2.model.ComicsCharacter;
import fr.tse.fise2.ui.components.CharacterCard;
import fr.tse.fise2.ui.panels.MainPanel;

class CharacterTileTest {
    private ComicsCharacter character;

    @BeforeEach
    void setUp() {
        character =  new ComicsCharacter(
            "Spider-Man", 
            "Peter Parker", 
            "Spidey", 
            "http://photo-url.com", 
            "12/08/1962", 
            "A friendly neighborhood Spider-Man.", 
            "Bitten by a radioactive spider, Peter Parker now has spider-like abilities, including wall-crawling, enhanced strength, and the famous 'spider-sense'.", 
            Arrays.asList("Amazing Spider-Man #1", "Spider-Man #200"),  // Utilisation de Arrays.asList pour créer une liste
            Arrays.asList("Amazing Spider-Man #1", "Spider-Man #200"),
        "Marvel Comics",
            "http://publisher-image.com", 
            "New York", 
            "http://spiderman-api.com", 
            1
        );
    }

    @Test
    void testCharacterTileCreation() {
        CharacterCard tile = new CharacterCard(character);

        // Vérifie que le JPanel a été bien créé
        assertNotNull(tile);
        assertEquals(BorderLayout.class, tile.getLayout().getClass());

        // Vérifie que l'image est bien présente (non vide) ou que le bouton a un fond noir si l'image est absente
        JButton imageButton = (JButton) tile.getComponent(0); // Récupère le bouton de l'image
        assertNotNull(imageButton.getIcon()); // Assure que l'image a bien été définie
    }

    @Test
    void testMissingImage() {
        // Crée un personnage sans photo URL pour tester le cas où l'image est manquante
        ComicsCharacter characterWithoutImage = new ComicsCharacter(
            "Iron Man", 
            "Tony Stark", 
            "Shellhead", 
            null, 
            "29/05/1963", 
            "Genius billionaire philanthropist.", 
            "Tony Stark built a high-tech suit of armor to escape captivity, and now uses it to fight injustice as Iron Man.", 
            Arrays.asList("Tales of Suspense #39", "Iron Man #1"), 
            Arrays.asList("Tales of Suspense #39", "Iron Man #1"), 
            "Marvel Comics", 
            "http://ironman-publisher.com",  
            "Long Island, New York", 
            "http://ironman-api.com", 
            2
        );
        CharacterCard tile = new CharacterCard(characterWithoutImage);

        // Vérifie que le fond du bouton image est noir lorsque l'image est manquante
        JButton imageButton = (JButton) tile.getComponent(0); // Récupère le bouton de l'image
        assertEquals(Color.BLACK, imageButton.getBackground()); // Le fond doit être noir
        assertTrue(imageButton.getText().contains("No Image")); // Le texte doit être "No Image"
    }

    @Test
    void testCharacterTileNameAndTag() {
        CharacterCard tile = new CharacterCard(character);

        // Vérifie que le nom du personnage est présent
        JButton nameButton = (JButton) ((JPanel) tile.getComponent(1)).getComponent(0); // Récupère le bouton du nom
        assertEquals("Spider-Man", nameButton.getText()); // Le texte du bouton doit être "Spider-Man"

        // Vérifie que le tag "Character" est affiché
        JPanel bottomPanel = (JPanel) tile.getComponent(1); // Panneau du bas
        JPanel tagPanel = (JPanel) bottomPanel.getComponent(1); // Le panneau contenant le tag
        assertTrue(tagPanel.getComponentCount() > 0); // Vérifie que le tag (image et label) est présent
        JLabel tagLabel = (JLabel) tagPanel.getComponent(1);
        assertEquals("Character", tagLabel.getText()); // Le texte doit être "Character"
    }

    @Test
    void testButtonActions() {
        // Créer et initialiser le MainPanel et son ContentPanel avant le test
        JPanel contentPanel = new JPanel(new CardLayout());
        MainPanel.setContentPanel(contentPanel); // Nécessite d'ajouter cette méthode statique à MainPanel
        MainPanel.setCurrentPanel("somePanel"); // Définir un panel courant
        
        CharacterCard tile = new CharacterCard(character);
        
        // Vérifier que les boutons existent
        JButton imageButton = (JButton) tile.getComponent(0);
        JPanel bottomPanel = (JPanel) tile.getComponent(1);
        JButton nameButton = (JButton) bottomPanel.getComponent(0);
        
        assertNotNull(imageButton, "Image button should not be null");
        assertNotNull(nameButton, "Name button should not be null");
        
        // Vérifier que les boutons ont des ActionListeners
        assertTrue(imageButton.getActionListeners().length > 0);
        assertTrue(nameButton.getActionListeners().length > 0);
        
        // Simuler les clics en mode headless
        SwingUtilities.invokeLater(() -> {
            for (ActionListener listener : imageButton.getActionListeners()) {
                listener.actionPerformed(new ActionEvent(imageButton, ActionEvent.ACTION_PERFORMED, "click"));
            }
            
            for (ActionListener listener : nameButton.getActionListeners()) {
                listener.actionPerformed(new ActionEvent(nameButton, ActionEvent.ACTION_PERFORMED, "click"));
            }
        });
    }
}