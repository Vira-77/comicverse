package fr.tse.fise2.api;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import fr.tse.fise2.exception.ApiException;
import fr.tse.fise2.model.Comic;
import fr.tse.fise2.service.SearchService;

// TODO: Revoir le fonctionnement de cette classe comme pour CharacterExtractor (regardez la classe ComicsCharacter pour les informations nécessaires et CharacterExtractor pour savoir comment les récupérer sinon demander à Lauriane)
// TODO: faire 2 fonctions (getComicByName anciennement getComicInfo et getComicById) pour récupérer les informations d'un comic par son nom ou son ID (encore une fois regardez la classe ComicsCharacter pour les informations nécessaires et CharacterExtractor pour savoir comment les récupérer sinon demander à Lauriane)

/**
 * Classe pour extraire les informations des comics
 * 
 * @version 1.0
 * @author Vivien KIM et Lauriane TUTALAMO
 */ 
public class ComicExtractor {

	public static List<Comic> getComicInfo(ApiClient apiClient, String queryParams) throws ApiException {
		String endpoint = "volumes";
		
		// Appel à l'API pour récupérer la liste des personnages correspondant au nom
		JsonNode response = apiClient.sendRequest(endpoint, queryParams);

		List<Comic> comicInfoList = new ArrayList<>();

		if (response.has("results") && response.get("results").size() > 0) {
			JsonNode comicsNode = response.get("results");

			// Itérer sur chaque personnage retourné
			for (JsonNode comicNode : comicsNode) {
				// Nom du volume associé à l'issue
				String issueName = comicNode.has("name") ? comicNode.get("name").asText() : "Nom inconnu";

				// URL image
				String imageUrl = null;
				if (comicNode.has("image") && comicNode.get("image").has("small_url")) {
					imageUrl = comicNode.get("image").get("small_url").asText();
				}
				
				// URL des détails
				String apiDetailUrl= comicNode.has("api_detail_url") ? comicNode.get("api_detail_url").asText() : "Inconnu";
				
				// Résumé succinct
				String deck=comicNode.has("deck") ? comicNode.get("deck").asText() : "Inconnu";
				
				// Description détaillée
				String description =comicNode.has("description") ? comicNode.get("description").asText() : "Inconnu";
				
				// Date de publication imprimée sur la couverture
				String coverDate=comicNode.has("cover_date") ? comicNode.get("cover_date").asText() : "Inconnu";
				
				// Retrieve people
				List<String> personCredits = new ArrayList<>();
				if (comicNode.has("people")) {
					JsonNode peopleNode = comicNode.get("people");
					for (JsonNode personNode : peopleNode) {
						personCredits.add(personNode.get("name").asText());
					}
				}

				// TODO: Bien sûr qu'il n'y a pas ces informations pour l'instant, il faut les récupérer (car elles sont primordiales pour l'affichage des détails du comic et peut être pour la recommandation de Bob) en appelant getComicById (à créer) pour récupérer les informations détaillées du comic (voir la classe CharacterExtractor pour avoir une idée de comment faire sinon demander à Lauriane)
				// pas les infos
				List<String> aliases = new ArrayList<>();
				List<String> comicCredits = new ArrayList<>();



				//id
				int id = comicNode.has("id") ? comicNode.get("id").asInt(): 00;

				// Créer un objet Comic pour ce comic
				Comic comicInfo = new Comic(
						issueName,
						imageUrl,
						aliases,
						apiDetailUrl,
						comicCredits,
						personCredits, // Set personCredits
						deck,
						description,
						coverDate,
						id
				);

				// Ajouter l'objet Comic à la liste
				comicInfoList.add(comicInfo);
			}
		} else {
			throw new ApiException("Unable to load comics. Please try again later.");
		}
		return comicInfoList;	// Retourner la liste de issues
	}

    public static List<Comic> getComicsByName(ApiClient apiClient, String ComicName) throws ApiException {
        String endpoint = "volumes";
		String queryParams = "filter=name:" + SearchService.formatCharacterNameForUrl(ComicName) + "&field_list=name,image,deck,people,id&limit=20";
        // Appel à l'API pour récupérer la liste des personnages correspondant au nom
        JsonNode response = apiClient.sendRequest(endpoint, queryParams);
    
        List<Comic> comicList = new ArrayList<>();
    
        if (response.has("results") && response.get("results").size() > 0) {
            JsonNode comicsNode = response.get("results");
    
            for (JsonNode comicNode : comicsNode) {
                // Extraction des informations de base
                String comicName = comicNode.has("name") ? comicNode.get("name").asText() : "Unknown";
                String photoUrl = comicNode.has("image") && comicNode.get("image").has("original_url")
                        ? comicNode.get("image").get("original_url").asText()
                        : null;
                String shortDescription = comicNode.has("deck") ? comicNode.get("deck").asText() : "No description";
                int id = comicNode.has("id") ? comicNode.get("id").asInt() : -1;

				// Retrieve people
				List<String> personCredits = new ArrayList<>();
				if (comicNode.has("people")) {
					JsonNode peopleNode = comicNode.get("people");
					for (JsonNode personNode : peopleNode) {
						personCredits.add(personNode.get("name").asText());
					}
				}
    
                // Création d'un Comic incomplet
                Comic comic = new Comic(
                        comicName,
						photoUrl,
						null,
						null,
						null, // Set personCredits
						personCredits,
						shortDescription,
						null,
						null,
						id
				);
	
				// Ajout du Comic à la liste
   
				comicList.add(comic);
            }
        } else {
			throw new ApiException("Aucun résultat trouvé pour : " + ComicName);
        }
        
        return comicList;
    }

    public static Comic getComicByID(ApiClient apiClient, String id) throws ApiException {
        String endpoint = "volume/4050-" + id;
        String queryParams = "filter=id:" + id + "&field_list=name,image,date_added,deck,description,people,characters&limit=1"; // Champs nécessaires

		// Appel à l'API pour récupérer la liste des comics correspondant à l'ID
		JsonNode response = apiClient.sendRequest(endpoint, queryParams);

		if (response.has("results") && response.get("results").size() > 0) {
			JsonNode comicNode = response.get("results");

			// Nom du volume associé à l'issue
			String issueName = comicNode.has("name") ? comicNode.get("name").asText() : "Nom inconnu";
			if (issueName != null && issueName.equals("null")) {
				issueName = "Not specified by the ComicVine API.";
			}

			// URL image
			String imageUrl = null;
			if (comicNode.has("image") && comicNode.get("image").has("small_url")) {
				imageUrl = comicNode.get("image").get("small_url").asText();
			}
			if (imageUrl != null && imageUrl.equals("null")) {
				imageUrl = "Not specified by the ComicVine API.";
			}
			
			// Liste des personnages inclus dans le comic
			List<String> characterCredits = new ArrayList<>();
			if (comicNode.has("characters")) {
				JsonNode charactersNode = comicNode.get("characters");
				for (JsonNode characterNode : charactersNode) {
					characterCredits.add(characterNode.get("name").asText());
				}
			}

			// Liste des personnes ayant participé à la création du comic
			List<String> personCredits = new ArrayList<>();
			if (comicNode.has("people")) {
				JsonNode peopleNode = comicNode.get("people");
				for (JsonNode personNode : peopleNode) {
					personCredits.add(personNode.get("name").asText());
				}
			}

			// Résumé succinct
			String rawDeck = comicNode.has("deck") ? comicNode.get("deck").asText() : null;
			// Description détaillée
			String rawDescription = comicNode.has("description") ? comicNode.get("description").asText() : null;

			// Build final description logic
			String finalDescription;
			if (rawDeck != null && !rawDeck.equals("null")) {
				finalDescription = rawDeck;
			} else if (rawDescription != null && !rawDescription.equals("null")) {
				finalDescription = rawDescription;
			} else {
				finalDescription = "Not specified by the ComicVine API.";
			}
			
			// Date de publication imprimée sur la couverture
			String coverDate=comicNode.has("date_added") ? comicNode.get("date_added").asText() : "Inconnu";
			if (coverDate != null && coverDate.equals("null")) {
				coverDate = "Not specified by the ComicVine API.";
			}
		
			return new Comic(
				issueName,
				imageUrl,
				null,
				null,
				characterCredits,
				personCredits,
				finalDescription,
				finalDescription,
				coverDate,
				Integer.parseInt(id)
			);
		}
		
		return new Comic(
			"Nom inconnu",
			null,
			null,
			null,
			null,
			null,
			"Inconnu",
			"Inconnu",
			"Inconnu",
			Integer.parseInt(id)
		);
    }
	
	public static Comic getComicFromURL(ApiClient apiClient, String queryParams,String URL,int readStatus) throws ApiException {
		
		// Appel à l'API pour récupérer la liste des personnages correspondant au nom
		JsonNode response = apiClient.sendRequestFromURL(URL, queryParams);

		if (response.has("results") && response.get("results").size() > 0) {
			JsonNode comicNode = response.get("results");

			
			
				// Nom du volume associé à l'issue
				String issueName = comicNode.has("name") ? comicNode.get("name").asText() : "Nom inconnu";

				// URL image
				String imageUrl = null;
				if (comicNode.has("image") && comicNode.get("image").has("small_url")) {
					imageUrl = comicNode.get("image").get("small_url").asText();
				}
				
				// URL des détails
				String apiDetailUrl= comicNode.has("api_detail_url") ? comicNode.get("api_detail_url").asText() : "Inconnu";
				
				// Résumé succinct
				String deck=comicNode.has("deck") ? comicNode.get("deck").asText() : "Inconnu";
				
				// Description détaillée
				String description =comicNode.has("description") ? comicNode.get("description").asText() : "Inconnu";
				
				// Date de publication imprimée sur la couverture
				String coverDate=comicNode.has("cover_date") ? comicNode.get("cover_date").asText() : "Inconnu";
				

				List<String> aliases = new ArrayList<>();
				List<String> comicCredits = new ArrayList<>();

				//id
				int id = comicNode.has("id") ? comicNode.get("id").asInt(): 00;

				// Créer un objet CharacterInfo pour ce personnage
				Comic comicInfo = new Comic(
						issueName,
						imageUrl,
						aliases,
						apiDetailUrl,
						comicCredits,
						null, // TODO: null pour l'instant mais doit être rempli
						deck,
						description,
						coverDate,
						id
				);

				//gerer l'état du comic rapport au compte utilisateur

				comicInfo.setReadingStatus(readStatus);
				comicInfo.setInLibrary(true);

				// Ajouter l'objet CharacterInfo à la liste
				return comicInfo;
			
		} else {
			throw new ApiException("Unable to load comics. Please try again later.");
		}
		
	}
}