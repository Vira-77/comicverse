package fr.tse.fise2.ui.panels;

import java.awt.*;
import javax.swing.*;
import java.util.List;
import java.util.logging.Logger;

import fr.tse.fise2.model.Comic;
import fr.tse.fise2.model.ComicManager;
import fr.tse.fise2.ui.components.ComicCard;

/**
 * Classe CharacterGridPanel
 * Cette classe permet d'aligner les vignettes des comics dans un panel.
 * 
 * @version 2.0
 * @author Lauriane TUTALAMO
 */
public class ComicGridPanel extends JPanel {
    private static final Logger logger = Logger.getLogger(CharacterGridPanel.class.getName()); // Logger pour la classe

    private static final int GRID_COLS = 5; // Nombre de colonnes fixes
    private static final int GRID_ROWS = 2; // Nombre de lignes fixes
    private static final int TILE_WIDTH = 200; // Largeur d'une vignette
    private static final int TILE_HEIGHT = 350; // Hauteur d'une vignette
    private static final int COMICS_PER_PAGE = GRID_COLS * GRID_ROWS; // 25 comics par page

    private List<Comic> comics; // Liste des comics
    private int currentPage = 0; // Page actuellement affichée

    private JPanel gridPanel; // Panneau de la grille
    private JLabel pageLabel; // Label pour afficher le numéro de page

    private CardLayout cardLayout; // Layout pour changer de carte (vue)
    private JPanel cardsPanel; // Panneau contenant les cartes (vignettes et loading)
    
    // Constructeur
    public ComicGridPanel(List<Comic> comics) {
        this.comics = comics;

        setLayout(new BorderLayout());

        // Créer le CardLayout et le panneau contenant les cartes
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);

        // Créer le panneau de la grille
        gridPanel = new JPanel(new GridLayout(GRID_ROWS, GRID_COLS, 20, 20));

        // Créer et ajouter le LoadingPanel
        LoadingPanel loadingPanel = new LoadingPanel();
        cardsPanel.add(loadingPanel, "Loading");

        // Ajouter le panneau de la grille au CardLayout
        cardsPanel.add(gridPanel, "Grid");

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
            if ((currentPage + 1) * COMICS_PER_PAGE < comics.size()) {
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
    
    // Met à jour la grille pour afficher les comics de la page actuelle
    private void updateGrid() {
        logger.info("Mise à jour de la grille pour la page: " + (currentPage + 1));
        gridPanel.removeAll(); // Supprime tous les composants actuels
        int start = currentPage * COMICS_PER_PAGE;
        int end = Math.min(start + COMICS_PER_PAGE, comics.size());

        for (int i = start ; i < end ; i++) {
            boolean trouvé=false;
            for (Comic allComic : ComicManager.getComicsList()) {
                if (allComic.getId()==comics.get(i).getId()){
                    ComicCard tile = new ComicCard(allComic);
                    tile.setPreferredSize(new Dimension(TILE_WIDTH, TILE_HEIGHT));
                    gridPanel.add(tile);
                    trouvé=true;
                }
            }
            if (!trouvé) {
                ComicCard tile = new ComicCard(comics.get(i));
                tile.setPreferredSize(new Dimension(TILE_WIDTH, TILE_HEIGHT));
                gridPanel.add(tile);
            }
        }    

        // Ajouter des panneaux vides pour compléter la grille si nécessaire
        int emptySlots = COMICS_PER_PAGE - (end - start);
        for (int i = 0; i < emptySlots; i++) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setPreferredSize(new Dimension(TILE_WIDTH, TILE_HEIGHT));
            emptyPanel.setOpaque(false);
            gridPanel.add(emptyPanel);
        }
        gridPanel.revalidate();
        gridPanel.repaint();

        logger.info("Page " + (currentPage + 1) + " affichée");
    }

    // Met à jour le label de la page
    private void updatePageLabel() {
        int totalPages = (int) Math.ceil((double) comics.size() / COMICS_PER_PAGE);
        pageLabel.setText("Page " + (currentPage + 1) + " / " + totalPages);
        logger.info("Mise à jour du label de page: Page " + (currentPage + 1) + " / " + totalPages);
    }
}