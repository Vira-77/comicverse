package fr.tse.fise2.controller;

import fr.tse.fise2.exception.ApiException;
import fr.tse.fise2.model.Comic;
import fr.tse.fise2.service.ComicService;

import java.util.List;

/**
 * Classe contrôleur pour les recommandations générales
 * Elle utilise le service ComicService pour récupérer les recommandations générales.
 * 
 * @version 1.0
 * @author Samuel PLET
 */
public class GeneralRecommandationController {
	private ComicService comicService;

	public GeneralRecommandationController() throws ApiException {
		this.comicService = new ComicService();
	}

	public List<Comic> fetchGeneralRecommendations() throws ApiException {
		return comicService.fetchGeneralRecommendations();
	}
}