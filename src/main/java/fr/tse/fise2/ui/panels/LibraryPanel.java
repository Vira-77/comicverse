package fr.tse.fise2.ui.panels;

import java.awt.*;
import javax.swing.*;
import java.util.List;

import javax.swing.plaf.basic.BasicScrollBarUI;

import fr.tse.fise2.model.Comic;
import fr.tse.fise2.model.ComicManager;
import fr.tse.fise2.ui.components.ComicCard;

public class LibraryPanel extends JPanel {
    
    private static final int TILE_WIDTH = 200; // Largeur d'une vignette
    private static final int TILE_HEIGHT = 500; // Hauteur d'une vignette
	
    // Define the panel for each card
    private static JPanel lecturePrevuePanel;
    private static JPanel lectureEnCoursPanel;
    private static JPanel lectureFiniePanel;
    private static JPanel allLibraryPanel;

    // CardLayout to manage switching between panels
    private CardLayout cardLayout;
    
    private static List<Comic> comics;

    public LibraryPanel () {
        // Set the layout to CardLayout
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        
        // Initialize each panel
        lecturePrevuePanel = createPanel(comics, 0);
        lectureEnCoursPanel = createPanel(comics, 1);
        lectureFiniePanel = createPanel(comics, 2);
        allLibraryPanel =createPanel(comics,3);

        // Add the panels to the CardLayout
        add(lecturePrevuePanel, "Plan to read");
        add(lectureEnCoursPanel, "reading");
        add(lectureFiniePanel, "Completed");
        add(allLibraryPanel,"all");
    }

    // Method to create the individual panels
    private JPanel createPanel(List<Comic> comics, int choice) {
        JPanel globalPanel = new JPanel();
        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setLayout(new GridLayout(0, 4, 20, 20));
        
        for (Comic comic : ComicManager.getComicsList()) {
            if (comic.getReadingStatus() == choice) {
            panel.add(new ComicCard(comic));
            }
            else if (comic.getInLibrary()==true){
                panel.add(new ComicCard(comic));
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

        // panel pour switch entre les panels
        JPanel navPanel =new JPanel();
        navPanel.setLayout(new FlowLayout());
        // Button to switch to "Lecture prévue"
        JButton lecturePrevueButton = new JButton("Plan to read");
        lecturePrevueButton.setBackground(new Color(0, 123, 255)); // Light blue
        lecturePrevueButton.setForeground(Color.WHITE); // White text
        lecturePrevueButton.addActionListener(e -> cardLayout.show(this, "Plan to read"));
    
        navPanel.add(lecturePrevueButton);
        
        // Button to switch to "Lecture en cours"
        JButton lectureEnCoursButton = new JButton("Reading");
        lectureEnCoursButton.addActionListener(e -> cardLayout.show(this, "reading"));
        lectureEnCoursButton.setBackground(new Color(0, 123, 255)); // Light blue
        lectureEnCoursButton.setForeground(Color.WHITE); // White text
        navPanel.add(lectureEnCoursButton);
        
        // Button to switch to "Lecture finie"
        JButton lectureFinieButton = new JButton("Completed");
        lectureFinieButton.addActionListener(e -> cardLayout.show(this, "Completed"));
        lectureFinieButton.setBackground(new Color(0, 123, 255)); // Light blue
        lectureFinieButton.setForeground(Color.WHITE); // White text
        navPanel.add(lectureFinieButton);

        // Button to switch to "allComics"
        JButton all= new JButton("All");
        all.addActionListener(e -> cardLayout.show(this, "all"));
        navPanel.add(all);
        
        globalPanel.add(navPanel, BorderLayout.SOUTH);
        
        return globalPanel;
    }
    
    // Method to refresh the panel content
    public static void refreshPanel(JScrollPane scrollPane, int choice) {
        // Get the panel inside the JScrollPane (assuming it's a JPanel)
        JPanel panel = (JPanel) scrollPane.getViewport().getView();

        // Remove all current components from the panel (clearing the old comics)
        panel.removeAll();  

        // Recreate the panel with the updated comics
        JPanel newPanel = new JPanel();
        newPanel.setBackground(Color.white);
        newPanel.setLayout(new GridLayout(0, 4, 20, 20));

        for (Comic comic : ComicManager.getComicsList()) {
            if (comic.getReadingStatus() == choice) {
                ComicCard comicCard=new ComicCard(comic);
                comicCard.setPreferredSize(new Dimension(TILE_WIDTH, TILE_HEIGHT));
                newPanel.add(comicCard);
            }
            else if (comic.getInLibrary()==true&& choice==3){
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
    
    // Method to refresh all three panels at the same time
    public static void refreshAllPanels() {
        // Refresh each panel by calling refreshPanel for each one
        refreshPanel((JScrollPane) lecturePrevuePanel.getComponent(0), 0); // "Lecture prévue"
        refreshPanel((JScrollPane) lectureEnCoursPanel.getComponent(0), 1); // "Lecture en cours"
        refreshPanel((JScrollPane) lectureFiniePanel.getComponent(0), 2); // "Lecture finie"
        refreshPanel((JScrollPane) allLibraryPanel.getComponent(0), 3); // "all"
    }

    // Method to add a new comic card to the panels
    public static void addComicCard(Comic comic) {
        // Create a new ComicCard
        ComicCard newCard1 = new ComicCard(comic);
        ComicCard newCard2 = new ComicCard(comic);
    
        // Get the correct panel depending on the comic's status
        int status = comic.getReadingStatus(); // 0 for 'Plan to Read', 1 for 'Reading', 2 for 'Completed'
    
        // Check if card is being added correctly to each panel
        System.out.println("Adding comic card to the panels...");
        
        if (status == 0) {
            // Get the panel inside the JScrollPane for "Plan to Read"
            JPanel panel = (JPanel) ((JScrollPane) lecturePrevuePanel.getComponent(0)).getViewport().getView();
            panel.add(newCard1);
            System.out.println("Added to 'Plan to Read' panel");
        } else if (status == 1) {
            // Get the panel inside the JScrollPane for "Reading"
            JPanel panel = (JPanel) ((JScrollPane) lectureEnCoursPanel.getComponent(0)).getViewport().getView();
            panel.add(newCard1);
            System.out.println("Added to 'Reading' panel");
        } else if (status == 2) {
            // Get the panel inside the JScrollPane for "Completed"
            JPanel panel = (JPanel) ((JScrollPane) lectureFiniePanel.getComponent(0)).getViewport().getView();
            panel.add(newCard1);
            System.out.println("Added to 'Completed' panel");
        }  
    
        // Get the panel inside the JScrollPane for "All Comics"
        JPanel allPanel = (JPanel) ((JScrollPane) allLibraryPanel.getComponent(0)).getViewport().getView();
        allPanel.add(newCard2);
        System.out.println("Added to 'All Comics' panel");    
    }

    // Method to remove a comic card from the panels
    public static void removeComicCard(Comic comic) {
        // Get the correct panel depending on the comic's status
        int status = comic.getReadingStatus(); // 0 for 'Plan to Read', 1 for 'Reading', 2 for 'Completed'
    
        // Remove the comic card from the corresponding panel
        if (status == 0) {
            removeCardFromPanel(lecturePrevuePanel, comic);
        } else if (status == 1) {
            removeCardFromPanel(lectureEnCoursPanel, comic);
        } else if (status == 2) {
            removeCardFromPanel(lectureFiniePanel, comic);
        }
    
        // Also remove the comic card from the "All Comics" panel
        removeCardFromPanel(allLibraryPanel, comic);
    }
    
    // Helper method to remove a specific card from the panel
    private static void removeCardFromPanel(JPanel panel, Comic comic) {
        // Get the panel inside the JScrollPane
        JPanel scrollPanel = (JPanel) ((JScrollPane) panel.getComponent(0)).getViewport().getView();
    
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

    // Method to switch to a specific card
    public void showCard(String cardName) {
        cardLayout.show(this, cardName);
    }
    
    public static void setComics(List<Comic> comicsList) {
        comics = comicsList;
    }
    public static List<Comic> getComics() {
        return comics;
    }

    public static JPanel getLecturePrevuePanel(){
        return lecturePrevuePanel;
    }

    public static JPanel getLectureEnCoursPanel(){
        return lectureEnCoursPanel;
    }

    public static JPanel getLectureFiniePanel(){
        return lectureFiniePanel;
    }

    public static JPanel getAllPanel(){
        return  allLibraryPanel;
    }
}