# Documentation Utilisateur - ComicVerse

## Introduction

Bienvenue dans ComicVerse, une application Java permettant d'explorer l'univers des comics via l'API Comic Vine. Cette documentation vous guidera à travers l'installation, les fonctionnalités et l'utilisation de l'application.

## Installation

### Prérequis

- Java 17
- Maven

### Étapes d'installation

1. **Cloner le repository :**
   ```bash
   git clone git@devops.telecomste.fr:prinfo/2024-25/info10.git
   cd info10
   ```

2. **Configurer le fichier `config.properties` :**
   - Créez le fichier `src/main/resources/config.properties`.
   - Ajoutez votre clé API Comic Vine :
     ```properties
     api.key=VOTRE_CLE_API
     ```
    - Voir le fichier `config.properties.example` pour un exemple.

3. **Compiler avec Maven :**
   ```bash
   mvn clean package
   ```

4. **Exécuter le JAR généré :**
   ```bash
   java -jar target/ComicVerse-1.0-SNAPSHOT.jar
   ```

## Fonctionnalités

- **Recherche :** Recherchez des comics et des personnages via l'API Comic Vine.
- **Gestion de compte :** Système de connexion/inscription pour gérer votre compte.
- **Bibliothèque :** Gérez vos comics en cours de lecture.
- **Favoris :** Sauvegardez vos comics préférés.
- **Recommandations :** Recevez des suggestions basées sur les dernières sorties.
- **Recommandations personnalisées :** Recevez des recommandations basées sur vos lectures.

## Utilisation

### Connexion/Inscription

1. **Ouvrir l'application :**
   - Lancez l'application en exécutant le JAR généré.

2. **Connexion :**
   - Cliquez sur l'icone de profil en haut à droite.
   - Entrez votre nom d'utilisateur et votre mot de passe.
   - Cliquez sur "Login".

3. **Inscription :**
   - Cliquez sur l'icone de profil en haut à droite.
   - Cliquez sur "Create an account".
   - Remplissez les champs requis et cliquez sur "Sign Up".

### Recherche de Comics

1. **Utiliser la barre de recherche :**
   - Entrez le nom d'un comic ou d'un personnage dans la barre de recherche.
   - Cliquez sur l'icône de recherche.

2. **Résultats de recherche :**
   - Parcourez les résultats et cliquez sur un comic pour voir plus de détails.

### Gestion de la Bibliothèque

1. **Ajouter un comic à la bibliothèque :**
   - Depuis les résultats de recherche, cliquez sur "Add to Library" en forme de "+".

2. **Voir la bibliothèque :**
   - Accédez à l'onglet "My Library" pour voir tous vos comics ajoutés.

### Favoris

1. **Ajouter un comic aux favoris :**
   - Depuis les résultats de recherche ou la bibliothèque, cliquez sur "Add to Favorites" en forme de coeur.

2. **Voir les favoris :**
   - Accédez à l'onglet "Favorites" pour voir tous vos comics favoris.

### Recommandations

1. **Voir les recommandations :**
   - Accédez à l'onglet "Home" pour voir les suggestions basées sur les dernières sorties.

### Recommandations personnalisées

1. **Voir les recommandations personnalisées :**
   - Accédez à l'onglet "For You" pour voir les recommandations basées sur vos lectures.

## FAQ

### Comment obtenir une clé API Comic Vine ?

- Rendez-vous sur le site de Comic Vine et inscrivez-vous pour obtenir une clé API.

### Que faire si l'application ne se lance pas ?

- Assurez-vous d'avoir Java 17 et Maven installés.
- Vérifiez que vous avez correctement configuré le fichier `config.properties`.