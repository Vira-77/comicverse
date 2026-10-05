package fr.tse.fise2.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import fr.tse.fise2.exception.ApiException;
import fr.tse.fise2.model.ComicsCharacter;

/**
 * Classe CharacterInfoExtractor
 * Cette classe permet de questionner l'API et d'en extraire des informations de personnages.
 * 
 * @author Lauriane TUTALAMO
 */
 public class CharacterExtractor {

    /**
     * Récupère une liste de personnage triées par ordre alphabétique en utilisant le nom du personnage recherché.
     * Récupère uniquement les informations de base (nom, image, description courte, ID).
     *
     * @param apiClient  L'objet ApiClient pour effectuer les requêtes
     * @param characterName Le nom du personnage qu'on cherche
     * @return Un objet ComicsCharacter contenant les informations détaillées du personnage
     * @throws ApiException En cas d'erreur lors de la requête ou de traitement
     */
    public static List<ComicsCharacter> getCharacterByName(ApiClient apiClient, String characterName) throws ApiException {
        String endpoint = "characters";
        String queryParams = "filter=name:" + characterName + "&field_list=name,image,deck,id"; // Champs nécessaires
    
        // Appel à l'API pour récupérer la liste des personnages correspondant au nom
        JsonNode response = apiClient.sendRequest(endpoint, queryParams);
    
        List<ComicsCharacter> characterList = new ArrayList<>();
    
        if (response.has("results") && response.get("results").size() > 0) {
            JsonNode charactersNode = response.get("results");
    
            for (JsonNode characterNode : charactersNode) {
                // Extraction des informations de base
                String heroName = characterNode.has("name") ? characterNode.get("name").asText() : "Not specified by the comicVine API";
                String photoUrl = characterNode.has("image") && characterNode.get("image").has("original_url")
                        ? characterNode.get("image").get("original_url").asText()
                        : null;
                String shortDescription = characterNode.has("deck") ? characterNode.get("deck").asText() : "No description";
                int id = characterNode.has("id") ? characterNode.get("id").asInt() : -1;
    
                // Création d'un ComicsCharacter incomplet
                ComicsCharacter character = new ComicsCharacter(
                        heroName,
                        null, // realName
                        null, // aliases
                        photoUrl,
                        null, // birthDate
                        shortDescription,
                        null, // longDescription
                        null, // issueCredits
                        null, // volumeCredits
                        null, // publisherName
                        null, // publisherImage
                        null, // origin
                        null, // apiDetailUrl
                        id
                );
    
                characterList.add(character);
            }
        } else {
            throw new ApiException("Aucun résultat trouvé pour : " + characterName);
        }
    
        // Trier la liste des personnages par ordre alphabétique
        Collections.sort(characterList, Comparator.comparing(ComicsCharacter::getHeroName));
        
        return characterList;
    }
    
    
    /**
     * Récupère les informations détaillées d'un personnage en utilisant son ID.
     * Les informations détaillées comprennent le nom réel, les alias, la photo, la date de naissance, la description courte et longue, les crédits de publication, l'éditeur, l'origine et l'URL de détail de l'API.
     *
     * @param apiClient  L'objet ApiClient pour effectuer les requêtes
     * @param characterId L'ID du personnage quand on la recherchait dans les characters (exemple:1234) 
     * @return Un objet ComicsCharacter contenant les informations détaillées du personnage
     * @throws ApiException En cas d'erreur lors de la requête ou de traitement
     */
    public static ComicsCharacter getCharacterById(ApiClient apiClient, int characterId) throws ApiException {
        String id = String.valueOf(characterId);
        String endpoint = "character/4005-" + id;
        String queryParams = "";
    
        // Appel à l'API pour récupérer les détails du personnage
        JsonNode response = apiClient.sendRequest(endpoint, queryParams);
    
        if (response.has("results") && response.get("results").isObject()) {
            JsonNode characterNode = response.get("results");
    
            // Hero name
            String heroName = characterNode.has("name") ? 
                (characterNode.get("name").asText().equals("null") ? "Not specified by the comicVine API" : characterNode.get("name").asText()) 
                : "Not specified by the comicVine API";
    
            // Real name
            String realName = characterNode.has("real_name") ? 
                (characterNode.get("real_name").asText().equals("null") ? "Not specified by the comicVine API" : characterNode.get("real_name").asText()) 
                : "Not specified by the comicVine API";
    
            // Aliases
            String aliases = characterNode.has("aliases") ? 
                (characterNode.get("aliases").asText().equals("null") ? "Not specified by the comicVine API" : characterNode.get("aliases").asText()) 
                : "Not specified by the comicVine API";
    
            // Photo URL
            String photoUrl = characterNode.has("image") && characterNode.get("image").has("original_url")
                    ? characterNode.get("image").get("original_url").asText()
                    : null;
    
            // Birth date
            String birthDate = characterNode.has("birth") ? 
                (characterNode.get("birth").asText().equals("null") ? "Not specified by the comicVine API" : characterNode.get("birth").asText()) 
                : "Not specified by the comicVine API";
            
    
            // Short description
            String shortDescription = characterNode.has("deck") ? 
                (characterNode.get("deck").asText().equals("null") && characterNode.get("description").asText() == "null" ? "Not specified by the comicVine API" : characterNode.get("deck").asText()) 
                : "Not specified by the comicVine API";

            // Long description
            String longDescription = characterNode.has("description") ? 
                (characterNode.get("description").asText().equals("null") && characterNode.get("description").asText() == "null" ? "Not specified by the comicVine API" : characterNode.get("description").asText()) 
                : "Not specified by the comicVine API";

            // Issue credits
            List<String> issueCredits = new ArrayList<>();
            if (characterNode.has("issue_credits")) {
                for (JsonNode issue : characterNode.get("issue_credits")) {
                    if (issue.has("name") && !issue.get("name").asText().equals("null")) {
                        issueCredits.add(issue.get("name").asText());
                    }
                }
            }
    
            // Volume credits
            List<String> volumeCredits = new ArrayList<>();
            if (characterNode.has("volume_credits")) {
                for (JsonNode volume : characterNode.get("volume_credits")) {
                    if (volume.has("name")) {
                        volumeCredits.add(volume.get("name").asText());
                    }
                }
            }
    
            // Publisher
            String publisherName = "Not specified by the comicVine API";
            String publisherImage = null;
            if (characterNode.has("publisher")) {
                JsonNode publisherNode = characterNode.get("publisher");
                publisherName = publisherNode.has("name") ? publisherNode.get("name").asText() : "Not specified by the comicVine API";
                publisherImage = publisherNode.has("image") ? publisherNode.get("image").get("original_url").asText() : null;
            }
    
            // Origin
            String origin = characterNode.has("origin") && characterNode.get("origin").has("name")
                    ? characterNode.get("origin").get("name").asText()
                    : "Not specified by the comicVine API";
    
            // API URL
            String apiDetailUrl = characterNode.has("api_detail_url") ? characterNode.get("api_detail_url").asText() : "Not available";
    
            // Créer un objet ComicsCharacter complet
            return new ComicsCharacter(
                    heroName,
                    realName,
                    aliases,
                    photoUrl,
                    birthDate,
                    shortDescription,
                    longDescription,
                    issueCredits,  
                    volumeCredits,
                    publisherName,
                    publisherImage,
                    origin,
                    apiDetailUrl,
                    characterId
            );
        } else {
            throw new ApiException("Aucun détail trouvé pour l'ID du personnage : " + characterId);
        }
        
    }    
}
