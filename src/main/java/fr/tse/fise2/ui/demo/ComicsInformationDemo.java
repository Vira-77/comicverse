package fr.tse.fise2.ui.demo;

import java.util.Arrays;

import javax.swing.JFrame;

import fr.tse.fise2.model.Comic;
import fr.tse.fise2.ui.ComicsInformation;

public class ComicsInformationDemo {
    public static void main(String[] args) {
    JFrame frame = new JFrame("Comic Information");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(1000, 800);

    // Create a sample ComicInfo object
    Comic sampleComic = new Comic("Sample Comic", "http://example.com/image.jpg",
            Arrays.asList("Alias 1", "Alias 2"), "http://example.com", Arrays.asList("Character 1", "Character 2", "spider"),null,
            "This is a sample synopsis of the comic.", "This is a detailed description of the comic.",
            "2023-10-01",2);

    // Add the ComicsInformation panel to the frame
    ComicsInformation comicInfoPanel = new ComicsInformation(sampleComic);
    frame.add(comicInfoPanel);

    frame.setVisible(true);
}
}
