package fr.tse.fise2.ui.demo;

import fr.tse.fise2.model.Comic;
import fr.tse.fise2.ui.LoginFrame;
import fr.tse.fise2.ui.components.ComicCard;
import fr.tse.fise2.model.User;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * Classe ComicTileDemo
 * Cette classe permet de visualiser une vignette pour les comics pour le design.
 * Elle pourra être supprimée à long terme.
 */
public class ComicTileDemo {
    private JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ComicTileDemo().show();
        });
    }
    
    public ComicTileDemo() {
        LoginFrame.loggedInUser = new User("demo", "demo");
        initializeFrame();
    }

    private void initializeFrame() {
        frame = new JFrame("Comic Tile Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 500);
        frame.setLayout(new BorderLayout());
        
        Comic demoComic = createDemoComic();
        ComicCard comicTile = new ComicCard(demoComic);
        frame.add(comicTile, BorderLayout.CENTER);
        
        addWindowListener();
    }

    private Comic createDemoComic() {
        return new Comic(
            "Amazing Spider-Man #1",
            "https://www.nicepng.com/png/detail/46-465451_spider-man-png-spiderman-png.png",
            List.of(),
            "",
            List.of(),
            null,
            "Demo deck",
            "Demo description",
            "2024-03",
            4
        );
    }
    
    private void addWindowListener() {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cleanupDemo();
            }
        });
    }
    
    private void cleanupDemo() {
        if (LoginFrame.loggedInUser != null) {
            LoginFrame.loggedInUser = null;
            System.out.println("Session de démo terminée");
        }
    }
    
    public void show() {
        frame.setVisible(true);
    }
}