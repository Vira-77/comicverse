package fr.tse.fise2.service;

import fr.tse.fise2.api.ApiClient;
import fr.tse.fise2.api.ComicExtractor;
import fr.tse.fise2.exception.ApiException;
import fr.tse.fise2.model.Comic;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe service pour les comics qui utilise l'API pour récupérer les informations des comics
 * 
 * @version 1.0
 * @author Samuel PLET
 */
public class ComicService {
	private ApiClient apiClient;

	public ComicService() throws ApiException {
		this.apiClient = new ApiClient();
	}

	public List<Comic> fetchGeneralRecommendations() throws ApiException {
		return ComicExtractor.getComicInfo(apiClient, "sort=date_added:desc&limit=20");
	}

	public List<Comic> fetchComicsByUrl(List<Comic> comics){
		List<Comic> comicList=comics;
		List<Comic> newComicList = new ArrayList<>();

		for (Comic comic: comicList) {
			try {
				Comic comicToAdd = ComicExtractor.getComicFromURL(apiClient,"",comic.getApiDetailUrl(),comic.getReadingStatus());
            if (comic != null) {
                newComicList.add(comicToAdd);
            }
				
			} catch (ApiException e) {
				System.out.println("erreur dans ComicService");
			}
        }
        return newComicList;
	}
}