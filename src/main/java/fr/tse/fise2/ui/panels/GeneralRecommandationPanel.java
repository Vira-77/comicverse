package fr.tse.fise2.ui.panels;
	
import java.awt.*;
import javax.swing.*;
import java.util.List;

import fr.tse.fise2.exception.ApiException;
import fr.tse.fise2.model.Comic;
import fr.tse.fise2.model.ComicManager;
import fr.tse.fise2.ui.components.ComicCard;
import fr.tse.fise2.ui.components.CustomScrollBarUI;
import fr.tse.fise2.controller.GeneralRecommandationController;

/**
 * Classe pour afficher les recommandations générales
 * 
 * @version 2.1
 * @author Vivien KIM, Lauriane TUTALAMO et Samuel PLET
 */
public class GeneralRecommandationPanel extends JPanel {

    private static final int TILE_WIDTH = 200; // Largeur d'une vignette
    private static final int TILE_HEIGHT = 500; // Hauteur d'une vignette

    private List<Comic> listGeneralRecommendation;  // Liste pour stocker les résultats de l'API
    private JPanel recommandationPanel;                 // Panel contenant les comics affichés
    private static final JLabel titleLabel = new JLabel("New", JLabel.CENTER); // Label pour le titre
    private GeneralRecommandationController controller;
            
    /**
     * Constructeur de la classe GeneralRecommandationPanel
     * Il initialise le contrôleur et récupère les recommandations générales
     */
    public GeneralRecommandationPanel() {
        // Initialiser le contrôleur
        try {
            controller = new GeneralRecommandationController();
        } catch (ApiException e) {
            e.printStackTrace();
        }

        // Définir le layout du panel
        setLayout(new BorderLayout()); 
        
        // Création du  titre
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        titleLabel.setOpaque(true);
        titleLabel.setForeground(Color.blue);
        titleLabel.setBackground(Color.black);
        add(titleLabel, BorderLayout.NORTH);
        
        try {
            // Initialiser la liste avec le contrôleur pour récupérer les données
            listGeneralRecommendation = controller.fetchGeneralRecommendations();
            
            // Créer le panel d'affichage des recommandations
            recommandationPanel = createPanelFromList(listGeneralRecommendation);

            // Ajouter le panel au scrollPane
            JScrollPane scrollPane = CustomScrollBarUI.createScrollPane(recommandationPanel);
            add(scrollPane, BorderLayout.CENTER);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode pour créer un panel à partir d'une liste de comics
     * @param comicList la liste de comics
     * @return le panel créé
     */
    private JPanel createPanelFromList(List<Comic> comicList) {
        JPanel panel = new JPanel(new GridLayout(0, 4, 20, 20));
        panel.setBackground(Color.white);

        for (Comic comic : comicList) {
            boolean trouvé=false;
            for (Comic allComic : ComicManager.getComicsList()) {
                if (allComic.getId()==comic.getId()) {
                    ComicCard comicCard=new ComicCard(allComic);
                    comicCard.setPreferredSize(new Dimension(TILE_WIDTH, TILE_HEIGHT));
                    panel.add(comicCard);
                    trouvé=true;
                    System.out.println("Le comic "+comic.getId()+" est déjà dans la bibliothèque.");
                }
            }
            if(!trouvé){
                ComicCard comicCard=new ComicCard(comic);
                comicCard.setPreferredSize(new Dimension(TILE_WIDTH, TILE_HEIGHT));
                panel.add(comicCard);
            }
        }
        System.out.println("Les boutons ont été mis à jour.");
        return panel;
    }

    // Méthode pour rafraîchir l'affichage à partir de la liste en mémoire
    public void refreshPanel() {
        if (listGeneralRecommendation != null) {
            // Supprimer l'ancien contenu du panel
            removeAll();
            add(titleLabel, BorderLayout.NORTH);

            // Recréer le panel à partir de la liste en mémoire
            recommandationPanel = createPanelFromList(listGeneralRecommendation);

            // Ajouter le nouveau panel au scrollPane
            JScrollPane scrollPane = CustomScrollBarUI.createScrollPane(recommandationPanel);

            // Mettre à jour l'interface graphique
            add(scrollPane, BorderLayout.CENTER);
            revalidate();
            repaint();
        }
    }
}