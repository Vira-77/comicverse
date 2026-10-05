package fr.tse.fise2.ui.panels;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import fr.tse.fise2.api.ApiClient;
import fr.tse.fise2.api.ComicExtractor;
import fr.tse.fise2.exception.ApiException;
import fr.tse.fise2.model.Comic;
import fr.tse.fise2.model.ComicManager;
import fr.tse.fise2.service.SearchService;
import fr.tse.fise2.ui.components.ComicCard;

/**
 * Panel to display recommended comics based on the user's library.
 * Recommendations are based on comics that share the same characters as those in the user's library.
 * 
 * @author Anthony SILVA-RAYNAL
 */
public class ForYouPanel extends JPanel {
    private JPanel comicsGridPanel;
    private JScrollPane scrollPane;

    public ForYouPanel() {
        setLayout(new BorderLayout());
        initializeComponents();
        loadRecommendedComics();
    }

    private void initializeComponents() {
        comicsGridPanel = new JPanel();
        comicsGridPanel.setLayout(new GridLayout(0, 3, 10, 10)); // 3 columns with spacing

        scrollPane = new JScrollPane(comicsGridPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Increases scroll speed
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadRecommendedComics() {
        System.out.println("=== Loading Recommended Comics ===");
    
        // 1. Build the user's library
        List<Comic> userLibrary = new ArrayList<>();
        for (Comic comic : ComicManager.getComicsList()) {
            if (ComicManager.isComicInLibrary(comic)) {
                try {
                    ApiClient apiclient = new ApiClient();
                    Comic comicCompleted = ComicExtractor.getComicByID(apiclient, Integer.toString(comic.getId()));
                    userLibrary.add(comicCompleted);
                } catch (ApiException e) {
                    JOptionPane.showMessageDialog(this,
                        "Error loading comic details: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                System.out.println("Comic not in library: " + comic.getName());           
            }
        }
        System.out.println("User library size: " + userLibrary.size());
        for (Comic c : userLibrary) {
            System.out.println(" -> Library Comic: " + c.getName() + 
                               ", Characters: " + c.getCharacterCredits());
        }
    
        // 2. Gather up to 3 main characters from the library
        Set<String> characters = new HashSet<>();
        int count = 0;
        for (Comic comic : userLibrary) {
            List<String> charCredits = comic.getCharacterCredits();
            if (charCredits != null && !charCredits.isEmpty()) {
                String mainCharacter = charCredits.get(0);
                System.out.println("Main character chosen: " + mainCharacter);
                characters.add(mainCharacter);
                count++;
                if (count == 1) break;
            }
        }
        System.out.println("Characters to search for: " + characters);
    
        // 3. Fetch recommended comics
        Set<Comic> recommendedComics = new HashSet<>();
        try {
            ApiClient apiClient = new ApiClient();
            for (String character : characters) {
                System.out.println("Calling API for character: " + character);
                List<Comic> comicsWithCharacter = ComicExtractor.getComicsByName(apiClient, SearchService.formatCharacterNameForUrl(character));
    
                System.out.println("API returned " + comicsWithCharacter.size() + " comics for character: " + character);
                for (Comic c : comicsWithCharacter) {
                    System.out.println(" -> " + c.getName());
                }
                recommendedComics.addAll(comicsWithCharacter);
            }
        } catch (ApiException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading recommended comics: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    
        // 4. Remove comics already in user's library
        System.out.println("Recommended set BEFORE removing library: " + recommendedComics.size());
        recommendedComics.removeAll(userLibrary);
        System.out.println("Recommended set AFTER removing library: " + recommendedComics.size());
    
        // 5. Populate the grid with recommended comics
        comicsGridPanel.removeAll(); // in case we're reloading
        System.out.println("Adding comics to grid...");
        int j = 0;
        for (Comic comic : recommendedComics) {
            if (j >= 6) break;
            if (ComicManager.isComicInLibrary(comic)) {
                System.out.println("Skipping comic already in library: " + comic.getName());
                continue;
            }
            ComicCard comicCard = new ComicCard(comic);
            comicsGridPanel.add(comicCard);
            j++;
        }
    
        comicsGridPanel.revalidate();
        comicsGridPanel.repaint();
    
        System.out.println("=== End Loading Recommended Comics ===\n");
    }
    
    
    public void reloadRecommendations() {
        // Clear existing content
        comicsGridPanel.removeAll();
    
        // Re-run the recommendation logic
        loadRecommendedComics();
    }
    
    @Override
    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);
        if (isVisible) {
            reloadRecommendations();
        }
    }
}
