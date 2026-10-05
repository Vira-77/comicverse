package fr.tse.fise2.controller;

import fr.tse.fise2.model.Comic;
import fr.tse.fise2.service.FavoriteService;

import java.util.List;

/**
 * Classe contrôleur pour les favoris de l'utilisateur.
 * Elle utilise le service FavoriteService pour gérer les favoris.
 * 
 * @version 1.0
 * @author Samuel PLET
 */
public class FavoriteController {

	private final FavoriteService favoriteService;

	/**
	 * Constructeur de la classe FavoriteController
	 * Il initialise le service FavoriteService
	 */
	public FavoriteController() {
		this.favoriteService = new FavoriteService();
	}

	/**
	 * Récupérer la liste des favoris
	 * @return la liste des favoris
	 */
	public List<Comic> getFavorites() {
		return favoriteService.getFavorites();
	}

	/**
	 * Ajouter un comic aux favoris
	 * @param comic le comic à ajouter
	 */
	public void addFavorite(Comic comic) {
		favoriteService.addFavorite(comic);
	}

	/**
	 * Supprimer un comic des favoris
	 * @param comic le comic à supprimer
	 */
	public void removeFavorite(Comic comic) {
		favoriteService.removeFavorite(comic);
	}

	/**
	 * Filtrer les favoris en fonction du texte de recherche
	 * @param query le texte de recherche
	 * @return la liste des favoris filtrée
	 */
	public List<Comic> filterFavorites(String query) {
		return favoriteService.filterFavorites(query);
	}
}