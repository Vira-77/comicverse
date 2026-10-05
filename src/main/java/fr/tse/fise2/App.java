package fr.tse.fise2;

import java.awt.*;
import javax.swing.*;

import java.io.IOException;
import java.util.logging.LogManager;

import fr.tse.fise2.exception.ApiException;
import fr.tse.fise2.ui.panels.MainPanel;

/**
 * Point d'entrée de l'application
 */
public class App {

	private static final String LOGGING_PROPERTIES = "logging.properties";
	private static final String APP_LOGO_PATH = "/images/app_logo.png";
	
	public static void main(String[] args) {

		// Charger la configuration du logger
		try {
			LogManager.getLogManager().readConfiguration(
				App.class.getClassLoader().getResourceAsStream(LOGGING_PROPERTIES)
			);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		// Charger l'icone de l'application
		ImageIcon icon = new ImageIcon(App.class.getResource(APP_LOGO_PATH));

		// Initialize the main application window
		SwingUtilities.invokeLater(() -> {
			JFrame mainWindow = new JFrame("ComicVERSE");
			mainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
			mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			// Définir l'icône de l'application
			mainWindow.setIconImage(icon.getImage());

			// Ajouter la page d'accueil à la fenêtre principale
			try {
				mainWindow.add(new MainPanel(), BorderLayout.CENTER);
			} catch (ApiException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(mainWindow, "Failed to load main panel: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
			mainWindow.setVisible(true);
		});
	}
}