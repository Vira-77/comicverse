package fr.tse.fise2.controller;

import java.util.List;

import fr.tse.fise2.exception.ApiException;
import fr.tse.fise2.model.Comic;
import fr.tse.fise2.model.ComicsCharacter;
import fr.tse.fise2.service.SearchService;
import fr.tse.fise2.ui.panels.MainView;

/**
 * Contrôleur principal de l'application
 * 
 * @version 1.0
 * @author Samuel PLET
 */
public class MainController {
	private MainView mainview;
	private SearchService searchService;

	/**
	 * Constructeur du contrôleur principal
	 * @param mainPanel Référence à la page d'accueil
	 */
	public MainController(MainView mainview) throws ApiException {
		this.mainview = mainview;
		this.searchService = new SearchService();
	}

	/**
	 * Méthode appelée lorsqu'un utilisateur effectue une recherche par personnage.
	 * @param query Requête de recherche
	 */
	public void performSearchCharacters(String query) {
		if (query == null || query.isEmpty()) {
			mainview.showError("Please enter a search query.");
			return;
		}

		try {
			List<ComicsCharacter> characters = searchService.searchCharacters(query);
			mainview.updateContentPanel(mainview.createCharactersSearchResultsPanel(characters));
		} catch (ApiException e) {
			mainview.showError("Error while searching for characters: " + e.getMessage());
		}
	}

	/**
	 * Méthode appelée lorsqu'un utilisateur effectue une recherche par titre de comic
	 * @param query Requête de recherche
	 */
	public void performSearchComics(String query) {
		if (query == null || query.isEmpty()) {
			mainview.showError("Please enter a search query.");
			return;
		}

		try {
			List<Comic> comics = searchService.searchComics(query);
			mainview.updateContentPanel(mainview.createComicsSearchResultsPanel(comics));
		} catch (ApiException e) {
			mainview.showError("Error while searching for comics: " + e.getMessage());
		}
	}

	/**
	 * Méthode appelée après la connexion d'un utilisateur
	 */
	public void refreshAfterLogin() {
		mainview.refreshAfterLogin();
	}
}