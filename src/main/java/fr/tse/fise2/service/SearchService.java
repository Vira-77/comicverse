package fr.tse.fise2.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import fr.tse.fise2.api.ApiClient;
import fr.tse.fise2.api.CharacterExtractor;
import fr.tse.fise2.api.ComicExtractor;
import fr.tse.fise2.exception.ApiException;
import fr.tse.fise2.model.Comic;
import fr.tse.fise2.model.ComicsCharacter;

/**
 * Service de recherche
 * 
 * @version 1.0
 */
public class SearchService {

	private ApiClient apiClient;

	
	public SearchService() throws ApiException {
		this.apiClient = new ApiClient();
	}

	public List<ComicsCharacter> searchCharacters(String query) throws ApiException {
		String formattedQuery = formatCharacterNameForUrl(query);
		return CharacterExtractor.getCharacterByName(apiClient, formattedQuery);
	}

	public List<Comic> searchComics(String query) throws ApiException {
		return ComicExtractor.getComicsByName(apiClient, query);
	}

	public static String formatCharacterNameForUrl(String characterName) {
		try {
			return URLEncoder.encode(characterName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return characterName;
		}
	}
}