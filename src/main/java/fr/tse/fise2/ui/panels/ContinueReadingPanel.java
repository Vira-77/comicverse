package fr.tse.fise2.ui.panels;

import javax.swing.*;
import fr.tse.fise2.model.Comic;
import fr.tse.fise2.model.ComicManager;
import fr.tse.fise2.ui.components.ComicCard;

import java.awt.*;
import java.awt.event.*;
import java.util.Collections;
import java.util.List;

public class ContinueReadingPanel extends JPanel {
    private static final int TILE_WIDTH = 200; // Largeur d'une vignette
    private static final int TILE_HEIGHT = 500; // Hauteur d'une vignette

    private JPanel cardPanel;  // Container for the panels
    private CardLayout cardLayout;  // CardLayout to switch between panels
    private static JPanel panel1, panel2, comicPanel1, comicPanel2; // Panels for the two sections
    private JButton switchButton;  // Button to switch between panels
    private JButton refreshButton;  // Button to refresh the comics in the current panel

    public ContinueReadingPanel() {
        setLayout(new BorderLayout());  // Main layout for the panel
        initializePanels();  // Initialize the panels by calling the helper method
    }

    private void initializePanels() {
        // Initialize CardLayout and the panel container
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Panel for the first label and comic cards (Comics You Haven't Finished)
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());

        JLabel label1 = new JLabel("Comics You Haven't Finished", SwingConstants.CENTER);
        panel1.add(label1, BorderLayout.NORTH);

        comicPanel1 = new JPanel();
        comicPanel1.setLayout(new GridLayout(1, 5)); // Set GridLayout with 1 row and 5 columns
        panel1.add(comicPanel1, BorderLayout.CENTER);

        cardPanel.add(panel1, "Panel 1");

        // Panel for the second label and comic cards (Comics You Wanted to Start)
        panel2 = new JPanel();
        panel2.setLayout(new BorderLayout());

        JLabel label2 = new JLabel("Comics You Wanted to Start", SwingConstants.CENTER);
        panel2.add(label2, BorderLayout.NORTH);

        comicPanel2 = new JPanel();
        comicPanel2.setLayout(new GridLayout(1, 5)); // Set GridLayout with 1 row and 5 columns
        panel2.add(comicPanel2, BorderLayout.CENTER);

        cardPanel.add(panel2, "Panel 2");

        // Add the card panel (container for both panels) to the main layout
        add(cardPanel, BorderLayout.CENTER);

        // Initialize buttons
        initializeButtons();

        // Initial loading of comics
        refreshComics();
    }

    private void initializeButtons() {
        // Button to switch between panels
        switchButton = new JButton("Switch Panel");
        switchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Switch to the next panel
                cardLayout.next(cardPanel);
            }
        });

        // Button to refresh the current panel
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Refresh the comics in the currently visible panel
                refreshComics();
            }
        });

        // Add both buttons to the bottom panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(switchButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void refreshComics() {
        // Clear the current comics in both panels
        comicPanel1.removeAll();
        comicPanel2.removeAll();

        // Get a list of comics that are in the library
        List<Comic> comicsInLibrary = ComicManager.getComicsList();

        // Shuffle the comics randomly
        Collections.shuffle(comicsInLibrary);

        // Add 5 random comics to the first panel (Comics You Haven't Finished)
        int count = 0;
        for (Comic comic : comicsInLibrary) {
            if (count >= 5) break;
            if (comic.getReadingStatus() == 1) { // Reading status == 1 means comics not finished
                ComicCard comicCard=new ComicCard(comic);
                comicCard.setPreferredSize(new Dimension(TILE_WIDTH, TILE_HEIGHT));
                comicPanel1.add(comicCard);
                count++;
            }
        }

        // Add 5 random comics to the second panel (Comics You Wanted to Start)
        count = 0;
        for (Comic comic : comicsInLibrary) {
            if (count >= 5) break;
            if (comic.getReadingStatus() == 0) { // Reading status == 0 means comics not started
                ComicCard comicCard=new ComicCard(comic);
                comicCard.setPreferredSize(new Dimension(TILE_WIDTH, TILE_HEIGHT));
                comicPanel2.add(comicCard);
                count++;
            }
        }

        

        // Revalidate and repaint the panels to update the display
        comicPanel1.revalidate();
        comicPanel1.repaint();
        comicPanel2.revalidate();
        comicPanel2.repaint();
    }

    
}
