package fr.tse.fise2.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import fr.tse.fise2.ui.LoginFrame;
import fr.tse.fise2.ui.panels.FavoritePanel;

/**
 * Classe pour gérer les favoris de l'utilisateur.
 * 
 * @version 1.0
 * @author Lauriane TUTALAMO
 */
public class Favorite {
	private static final Logger LOGGER = Logger.getLogger(LoginFrame.class.getName());	

	// Liste statique représentant les favoris
	private static List<Comic> favorites = new ArrayList<>();

	// Référence statique à l'instance de FavoritePanel
	private static FavoritePanel favoritePanel;

	// Méthode pour définir l'instance de FavoritePanel
	public static void setFavoritePanel(FavoritePanel panel) {
		favoritePanel = panel;
	}

	public static void setFavorites(List<Comic> newFavorites) {
		favorites = newFavorites;
		System.out.println("Les favoris ont été mis à jour.");
	}

	// Méthode pour ajouter un comic aux favoris
	public static void addComicToFavorites(Comic comic) {
		if (!isFavorite(comic)) {
			favorites.add(comic);
			comic.setInFavorite(true);
			updateFavoritePanel(); // Rafraîchir le panneau des favoris

			try{
				ComicManager.addComicToFavorites(comic);
			} catch (Exception ex) {
				LOGGER.severe("Erreur d'ajout aux favoris: " + ex.getMessage());
			}
		}
	}

	// Méthode pour retirer un comic des favoris
	public static void removeComicFromFavorites(Comic comic) {
		if (favorites.remove(comic)) {
			comic.setInFavorite(false);
			updateFavoritePanel(); // Rafraîchir le panneau des favoris

			try {
				ComicManager.removeComicFromFavorites(comic);
			} catch (Exception ex) {
				LOGGER.severe("Erreur de suppression des favoris: " + ex.getMessage());
			}

		}
	}

	// Méthode pour récupérer la liste des comics favoris
	public static List<Comic> getFavorites() {
		return favorites;
	}

	// Méthode pour vérifier si un comic est déjà dans les favoris
	public static boolean isFavorite(Comic comic) {
		return favorites.contains(comic);
	}

	// Méthode pour réinitialiser les favoris (vider la liste)
	public static void resetFavorites() {
		favorites.clear();
		updateFavoritePanel(); // Rafraîchir le panneau des favoris
		System.out.println("Les favoris ont été réinitialisés.");
	}

	// Méthode pour mettre à jour dynamiquement le panneau des favoris
	private static void updateFavoritePanel() {
		if (favoritePanel != null) {
			FavoritePanel.loadFavorites(); // Utiliser la méthode existante dans FavoritePanel
		}
	}

	// Méthode pour récupérer l'instance de FavoritePanel
	static FavoritePanel getFavoritePanel() {
		return favoritePanel;
	}
}