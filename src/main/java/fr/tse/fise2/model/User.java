package fr.tse.fise2.model;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.io.IOException;

/**
 * Classe User
 * Cette classe représente un utilisateur de l'application ComicVerse
 * Elle permet de stocker les informations de l'utilisateur et de les manipuler
 * 
 * @version 1.0
 * @autor Samuel PLET
 */
public class User {
    private static final Logger LOGGER = Logger.getLogger(User.class.getName());
    private Integer id;
    private String username;
    private String password;
    private LocalDateTime createdAt;

    // Initialisation du logger
    static {
        try {
            // Créez le dossier log s'il n'existe pas
            File logDir = new File("./logs");
            if (!logDir.exists()) {
                logDir.mkdir();
            }
            
            // Configurez le FileHandler pour enregistrer les logs dans un fichier
            FileHandler fileHandler = new FileHandler("./logs/user.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.config("Logger initialisé avec succès.");
        } catch (IOException e) {
            LOGGER.severe("Erreur lors de la configuration du FileHandler : " + e.getMessage());
        }
    }

    // Constructeur pour nouvel utilisateur
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }

    // Constructeur pour utilisateur existant
    public User(Integer id, String username, String password, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
    }

    // Getters et setters
    public Integer getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Validation
    public boolean isValid() {
        return username != null && username.length() >= 3 && 
               password != null && password.length() >= 8;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}