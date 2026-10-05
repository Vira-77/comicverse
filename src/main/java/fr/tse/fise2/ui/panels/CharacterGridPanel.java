package fr.tse.fise2.ui.panels;

import java.awt.*;
import javax.swing.*;
import java.util.List;
import java.util.logging.Logger;

import fr.tse.fise2.model.ComicsCharacter;
import fr.tse.fise2.ui.components.CharacterCard;
import fr.tse.fise2.ui.components.CustomScrollBarUI;

/**
 * Classe CharacterGridPanel
 * Cette classe permet d'aligner les vignettes des personnages dans un panel.
 * 
 * @version 2.0
 * @author Lauriane TUTALAMO
 */
public class CharacterGridPanel extends JPanel {
    private static final Logger logger = Logger.getLogger(CharacterGridPanel.class.getName()); // Logger pour la classe

    private static final int GRID_COLS = 5; // Nombre de colonnes fixes
    private static final int GRID_ROWS = 3; // Nombre de lignes fixes
    private static final int CHARACTERS_PER_PAGE = GRID_COLS * GRID_ROWS; // 25 personnages par page

    private List<ComicsCharacter> characters; // Liste des personnages
    private int currentPage = 0; // Page actuellement affichée

    private JPanel gridPanel; // Panneau de la grille
    private JLabel pageLabel; // Label pour afficher le numéro de page

    private CardLayout cardLayout; // Layout pour changer de carte (vue)
    private JPanel cardsPanel; // Panneau contenant les cartes (vignettes et loading)
    
    public CharacterGridPanel(List<ComicsCharacter> characters) {
        this.characters = characters;

        setLayout(new BorderLayout());

        // Créer le CardLayout et le panneau contenant les cartes
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        
        // Créer le panneau de la grille
        gridPanel = new JPanel(new GridLayout(0, GRID_COLS, 10, 10));

        // Ajouter le panneau de la grille dans un JScrollPane personnalisé
        JScrollPane scrollPane = CustomScrollBarUI.createScrollPane(gridPanel);

        // Ajouter le panneau de favoris (grille avec barre de défilement) au CardLayout
        cardsPanel.add(scrollPane, "Grid");


        // Créer et ajouter le LoadingPanel
        LoadingPanel loadingPanel = new LoadingPanel();
        cardsPanel.add(loadingPanel, "Loading");

        // Ajouter le panneau de la grille au CardLayout
        //cardsPanel.add(gridPanel, "Grid");

        // Ajouter le panneau contenant les cartes (CardLayout) au CharacterGridPanel
        add(cardsPanel, BorderLayout.CENTER);

        // Créer le panneau de navigation
        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton prevButton = new JButton("← Previous");
        JButton nextButton = new JButton("Next →");

        // Définir le fond des boutons en noir
        prevButton.setBackground(Color.BLACK);
        nextButton.setBackground(Color.BLACK);

        // Définir la couleur du texte des boutons en blanc
        prevButton.setForeground(Color.WHITE);
        nextButton.setForeground(Color.WHITE);

        pageLabel = new JLabel(); // Affiche le numéro de page
        updatePageLabel();

        // Action pour le bouton "Précédent"
        prevButton.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                logger.info("Page précédente: " + currentPage);
                showLoadingAndUpdateGrid();
            } else {
                logger.warning("Tentative de revenir à une page invalide.");
            }
        });

        // Action pour le bouton "Suivant"
        nextButton.addActionListener(e -> {
            if ((currentPage + 1) * CHARACTERS_PER_PAGE < characters.size()) {
                currentPage++;
                logger.info("Page suivante: " + currentPage);
                showLoadingAndUpdateGrid();
            } else {
                logger.warning("Tentative d'aller au-delà du nombre total de pages.");
            }
        });

        // Ajouter les boutons et le label au panneau de navigation
        navigationPanel.add(prevButton);
        navigationPanel.add(pageLabel);
        navigationPanel.add(nextButton);

        // Ajouter le panneau de navigation en bas
        add(navigationPanel, BorderLayout.SOUTH);

        // Charger les données dès que la vue est créée
        showLoadingAndUpdateGrid();
    }

    // Méthode pour afficher le panneau de chargement, puis mettre à jour la grille
    private void showLoadingAndUpdateGrid() {
        // Afficher le panneau de chargement immédiatement
        cardLayout.show(cardsPanel, "Loading");
    
        // Mettre à jour la grille dans un autre thread pour éviter de bloquer l'interface
        new Thread(() -> {
            updatePageLabel(); // Met à jour le label de la page
            updateGrid(); // Met à jour la grille (opération potentiellement longue)
    
            // Retourner sur le thread principal pour basculer sur le panneau de grille
            SwingUtilities.invokeLater(() -> cardLayout.show(cardsPanel, "Grid"));
        }).start();
    }
    
    // Met à jour la grille pour afficher les personnages de la page actuelle
    public void updateGrid() {
        logger.info("Mise à jour de la grille pour la page: " + (currentPage + 1));
        gridPanel.removeAll(); // Supprime tous les composants actuels
        int start = currentPage * CHARACTERS_PER_PAGE;
        int end = Math.min(start + CHARACTERS_PER_PAGE, characters.size());

        // Ajouter les personnages de la page actuelle
        for (int i = start; i < end; i++) {
            CharacterCard tile = new CharacterCard(characters.get(i));
            gridPanel.add(tile);
        }
        gridPanel.revalidate();
        gridPanel.repaint();

        logger.info("Page " + (currentPage + 1) + " affichée");
    }

    // Met à jour le label de la page
    public void updatePageLabel() {
        int totalPages = (int) Math.ceil((double) characters.size() / CHARACTERS_PER_PAGE);
        pageLabel.setText("Page " + (currentPage + 1) + " / " + totalPages);
        logger.info("Mise à jour du label de page: Page " + (currentPage + 1) + " / " + totalPages);
    }

    //Getter et setter
    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        showLoadingAndUpdateGrid();
    }

    public List<ComicsCharacter> getCharacters() {
        return characters;
    }

    public void setCharacters(List<ComicsCharacter> characters) {
        this.characters = characters;
        showLoadingAndUpdateGrid();
    }
    public JPanel getGridPanel() {
        return gridPanel;
    }

    public void setGridPanel(JPanel gridPanel) {
        this.gridPanel = gridPanel;
    }

    public JLabel getPageLabel() {
        return pageLabel;
    }

    public void setPageLabel(JLabel pageLabel) {
        this.pageLabel = pageLabel;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public void setCardLayout(CardLayout cardLayout) {
        this.cardLayout = cardLayout;
    }

    public JPanel getCardsPanel() {
        return cardsPanel;
    }

    public void setCardsPanel(JPanel cardsPanel) {
        this.cardsPanel = cardsPanel;
    }
}