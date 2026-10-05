package fr.tse.fise2.model;

import java.io.File;
import java.io.IOException;

import java.util.*;
import java.util.logging.*;

import javax.swing.JOptionPane;

import fr.tse.fise2.ui.LoginFrame;

/**
 * Classe représentant la bibliothèque de l'utilisateur
 * 
 * @version 1.0
 */
public class Library {
	
	// Liste statique représentant la bibliothèque
    private static List<Comic> libraryList;
    private static final Logger LOGGER = Logger.getLogger(LoginFrame.class.getName());

    // Initialisation du logger
    static {
        try {
            // Créez le dossier log s'il n'existe pas
            File logDir = new File("./logs");
            if (!logDir.exists()) {
                logDir.mkdir();
            }
            
            // Configurez le FileHandler pour enregistrer les logs dans un fichier
            FileHandler fileHandler = new FileHandler("./logs/library.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.config("Logger initialisé avec succès.");
        } catch (IOException e) {
            LOGGER.severe("Erreur lors de la configuration du FileHandler : " + e.getMessage());
        }
    }

	public static void readingStatus(Comic comic) {
		
		String[] options = {"Planned to read", "Currently reading", "Completed"};
        int choice = JOptionPane.showOptionDialog(
        null,
        "Select the reading status of the comic:",
        "Choose a reading status",
        JOptionPane.DEFAULT_OPTION,
        JOptionPane.INFORMATION_MESSAGE,
        null, // No icon
        options, // The three options to choose from
        options[0] // Default option ("Planned to read")
        );

        // Handle the choice
        if (choice != -1) { // -1 means the user closed the dialog without making a choice
            
        }
        comic.setReadingStatus(choice);
        
        //System.out.println("Le comic " + comic.getName() + " a été ajouté à la bibliothèque.");
	}

    // Méthode pour savoir si un comic est dans la bibliothèque
	public static boolean isInLibrary(Comic comic) {
		return getComicsID().contains(comic.getId());	
	}

    // Méthode pour récupérer la liste des comics dans la bibliothèque
    public static List<Integer> getComicsID(){
        List<Integer> listId=new ArrayList<>();
        for(Comic comic :ComicManager.getComicsList()){
            listId.add(comic.getId());
        }
        return listId;
    }

    // Méthode pour reset la bibliothèque
    public static void resetLibrary() {
        if (libraryList != null) {
            libraryList.clear();
        }
    }
}