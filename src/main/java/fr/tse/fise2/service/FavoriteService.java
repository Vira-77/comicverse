package fr.tse.fise2.service;

import fr.tse.fise2.model.Comic;
import fr.tse.fise2.model.ComicManager;
import fr.tse.fise2.model.Favorite;

import java.util.List;

/**
 * Classe service pour les favoris qui utilise le FavoriteManager pour gérer les favoris.
 * 
 * @version 1.0
 * @author Samuel PLET
 */
public class FavoriteService {

	/**
	 * Récupérer la liste des favoris depuis le FavoriteManager
	 * @return la liste des favoris
	 */
	public List<Comic> getFavorites() {
		return Favorite.getFavorites();
	}

	/**
	 * Ajouter un comic aux favoris
	 * @param comic le comic à ajouter
	 */
	public void addFavorite(Comic comic) {
		Favorite.addComicToFavorites(comic);
	}

	/**
	 * Supprimer un comic des favoris
	 * @param comic le comic à supprimer
	 */
	public void removeFavorite(Comic comic) {
		Favorite.removeComicFromFavorites(comic);
	}

	/**
	 * Filtrer les favoris en fonction du texte de recherche
	 * @param query le texte de recherche
	 * @return la liste des favoris filtrée
	 */
	public List<Comic> filterFavorites(String query) {
		return ComicManager.getFavoriteComics().stream()
				.filter(comic -> comic.getName().toLowerCase().contains(query.toLowerCase()))
				.toList();
	}
}