package fr.tse.fise2.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.tse.fise2.exception.ApiException;

/**
 * Classe ApiClient
 * Cette classe est utilisée pour envoyer des requêtes à l'API Comic Vine. 
 * 
 * @version 1.0
 * @author Samuel PLET
 */
public class ApiClient {
    private static final Logger LOGGER = Logger.getLogger(ApiClient.class.getName());
    private static final String API_URL = "https://comicvine.gamespot.com/api/";
    private String apiKey;
    private HttpClient httpClient;
    private ObjectMapper objectMapper;

    // Initialisation du logger
    static {
        try {
            // Créez le dossier log s'il n'existe pas
            File logDir = new File("./logs");
            if (!logDir.exists()) {
                logDir.mkdir();
            }

            // Configurez le FileHandler pour enregistrer les logs dans un fichier
            FileHandler fileHandler = new FileHandler("./logs/apiclient.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.config("Logger initialisé avec succès.");
        } catch (IOException e) {
            LOGGER.severe("Erreur lors de la configuration du FileHandler : " + e.getMessage());
        }
    }

    // Constructeur
    public ApiClient() throws ApiException {
        this.apiKey = loadApiKey();
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        if (this.apiKey == null) {
            throw new ApiException("Clé API non trouvée dans le fichier de configuration.");
        }
        LOGGER.info("ApiClient initialisé avec succès.");
    }

    // Charge la clé API depuis le fichier de configuration
    private String loadApiKey() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                LOGGER.severe("Impossible de trouver config.properties");
                return null;
            }
            properties.load(input);
            LOGGER.config("Fichier config.properties chargé avec succès.");
            return properties.getProperty("api.key");
        } catch (IOException ex) {
            LOGGER.severe("Erreur lors du chargement de config.properties : " + ex.getMessage());
            return null;
        }
    }

    // Méthode générique pour envoyer des requêtes à l'API Comic Vine
    public JsonNode sendRequest(String endpoint, String queryParams) throws ApiException {
        try {
            LOGGER.info("Envoi de la requête à l'endpoint : " + endpoint + " avec les paramètres : " + queryParams);
            String url = String.format("%s%s/?api_key=%s&format=json&%s", API_URL, endpoint, apiKey, queryParams);
            LOGGER.info("URL complète : " + url);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "Java HttpClient")
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            JsonNode jsonResponse = objectMapper.readTree(responseBody);
            checkStatusCode(jsonResponse);
            LOGGER.info("Réponse reçue avec succès pour l'endpoint : " + endpoint + " avec les paramètres : " + queryParams);
            return jsonResponse;
        } catch (IOException | InterruptedException e) {
            LOGGER.severe("Erreur lors de l'envoi de la requête : " + e.getMessage());
            throw new ApiException("Erreur lors de l'envoi de la requête : " + e.getMessage(), e);
        }
    }

    // Vérifie le status_code de la réponse JSON
    private void checkStatusCode(JsonNode jsonResponse) throws ApiException {
        int statusCode = jsonResponse.path("status_code").asInt();
        if (statusCode != 1) {
            LOGGER.severe("Erreur de l'API Comic Vine : status_code = " + statusCode);
            throw new ApiException("Erreur de l'API Comic Vine : status_code = " + statusCode);
        }
        LOGGER.info("Status code vérifié avec succès : " + statusCode);
    }

    //méthode pour envoyer une requête à partir d'url stocké dans la base de données
    public JsonNode sendRequestFromURL(String queryParams,String URL) throws ApiException {

        try {
            
            
            String url = String.format("%s?api_key=%s&format=json&%s",URL, apiKey, queryParams);
            LOGGER.info("URL complète : " + url);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "Java HttpClient")
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            JsonNode jsonResponse = objectMapper.readTree(responseBody);
            checkStatusCode(jsonResponse);
            LOGGER.info("Réponse reçue avec succès pour l'url"+ URL);
            return jsonResponse;
        } catch (IOException | InterruptedException e) {
            LOGGER.severe("Erreur lors de l'envoi de la requête : " + e.getMessage());
            throw new ApiException("Erreur lors de l'envoi de la requête : " + e.getMessage(), e);
        }
    }

    // Getters
    public String getApiUrl() {
        return API_URL;
    }

    public String getApiKey() {
        return apiKey;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}