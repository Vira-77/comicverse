package fr.tse.fise2.ui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

import fr.tse.fise2.model.Comic;
import fr.tse.fise2.model.ComicManager;
import fr.tse.fise2.ui.components.ComicCard;

public class ShoppingPanel extends JPanel {

    // This list will hold the references to the comic cards for dynamic updates.
    private static JPanel shoppingPanel;
    private static final int TILE_WIDTH = 200; // Largeur d'une vignette
    private static final int TILE_HEIGHT = 500; // Hauteur d'une vignette

    public ShoppingPanel() {
        setLayout(new BorderLayout()); // Set the layout for the parent panel
        shoppingPanel = createPanel(ComicManager.getComicsList());
        add(shoppingPanel, BorderLayout.CENTER); // Add the shoppingPanel in the center
    }

    private JPanel createPanel(List<Comic> comics) {
        JPanel globalPanel = new JPanel();
        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setLayout(new GridLayout(0, 4, 20, 20));
        

        for (Comic comic : comics) {
            // Only add comics that are available for purchase
            if (comic.getInPurchase() == true) {
                ComicCard comicCard=new ComicCard(comic);
                comicCard.setPreferredSize(new Dimension(TILE_WIDTH, TILE_HEIGHT));
                panel.add(comicCard);
            }
        }

        globalPanel.setLayout(new BorderLayout());

        // Ajouter le navpanel au scrollPane
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(16);
        verticalScrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.CYAN;
                this.trackColor = Color.LIGHT_GRAY;
            }
        });
        
        globalPanel.add(scrollPane, BorderLayout.CENTER);
        return globalPanel;

    }

    // Add a new comic card to the panel dynamically
    public static void addComicCard(Comic comic) {
        // Only add if the comic is available for purchase
        if (comic.getInPurchase()) {
            ComicCard comicCard=new ComicCard(comic);
            comicCard.setPreferredSize(new Dimension(TILE_WIDTH, TILE_HEIGHT));
            shoppingPanel.add(comicCard);
            shoppingPanel.revalidate();  // Revalidate the layout
            shoppingPanel.repaint();  // Repaint the panel to show the new comic card
        }
    }

    // Remove a comic card from the panel dynamically
    public static void removeComicCard(Comic comic) {
       // Get the panel inside the JScrollPane
       JPanel scrollPanel = (JPanel) ((JScrollPane) shoppingPanel.getComponent(0)).getViewport().getView();
    
       // Iterate over the components to find the corresponding ComicCard
       for (int i = 0; i < scrollPanel.getComponentCount(); i++) {
           if (scrollPanel.getComponent(i) instanceof ComicCard) {
               ComicCard card = (ComicCard) scrollPanel.getComponent(i);
               if (card.getComic().equals(comic)) {
                   // Remove the card from the panel
                   scrollPanel.remove(i);
                   break;
               }
           }
       }
   
       // Revalidate and repaint the panel to update the view
       scrollPanel.revalidate();
       scrollPanel.repaint();
    }

    // Refresh the shopping panel (reload the comic cards)
    public static void refresh() {
        // Get the panel inside the JScrollPane (assuming it's a JPanel)
        JScrollPane scrollPane= (JScrollPane) shoppingPanel.getComponent(0);
        JPanel panel = (JPanel) scrollPane.getViewport().getView();
        

        // Remove all current components from the panel (clearing the old comics)
        panel.removeAll();  

        // Recreate the panel with the updated comics
        JPanel newPanel = new JPanel();
        newPanel.setBackground(Color.white);
        newPanel.setLayout(new GridLayout(0, 4, 20, 20));

        for (Comic comic :ComicManager.getComicsList()) {
            // Only add comics that are available for purchase
            if (comic.getInPurchase() == true) {
                 ComicCard comicCard=new ComicCard(comic);
                 comicCard.setPreferredSize(new Dimension(TILE_WIDTH, TILE_HEIGHT));
                 newPanel.add(comicCard);
            }
        }

        // Replace the old panel with the new one
        scrollPane.setViewportView(newPanel);

        // Revalidate and repaint the panel to update the layout
        newPanel.revalidate();
        newPanel.repaint();
    }
}