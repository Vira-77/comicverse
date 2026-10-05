package fr.tse.fise2.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Collectors;

import fr.tse.fise2.exception.ApiException;
import fr.tse.fise2.model.ComicsCharacter;

public class CharacterExtractorTest {
    private final ApiClient apiClient;

    public CharacterExtractorTest() throws ApiException {
        this.apiClient = new ApiClient();
    }
    
    @Test
    public void testGetCharacterByName() throws ApiException {
        // Test case 1
        // On vérifie qu'on a les informations essentielles et que les reste est null grâce au toString de ComicsCharacter
        String characterName = "Spider-Man";
        List<ComicsCharacter> characters = CharacterExtractor.getCharacterByName(apiClient, characterName);
        assertNotNull(characters);
        assertEquals(9, characters.size());
        assertEquals("Skrull Spider-Man", characters.get(0).getHeroName());
        assertEquals("https://comicvine.gamespot.com/a/uploads/original/3/31666/932012-skrull.jpg", characters.get(0).getPhotoUrl());
        assertEquals("Skrull that impersonated Spider-Man during Secret Invasion.", characters.get(0).getShortDescription());
        assertEquals(60535, characters.get(0).getId());
        assertEquals("Character Information:\n" +
        "----------------------\n" +
        "Hero Name: Skrull Spider-Man\n" +
        "Real Name: Unknown\n" +
        "Aliases: No aliases\n" +
        "Photo URL: https://comicvine.gamespot.com/a/uploads/original/3/31666/932012-skrull.jpg\n" +
        "Birth Date: Unknown\n" +
        "Short Description: Skrull that impersonated Spider-Man during Secret Invasion.\n" +
        "Long Description: No description\n" +
        "Issue Credits: None\n" +
        "Volume Credits: None\n" +
        "Publisher: Unknown\n" +
        "Publisher Image URL: No image\n" +
        "Origin: Unknown\n" +
        "API Detail URL: Unknown\n" +
        "ID: 60535\n", characters.get(0).toString());
    }

    @Test
    public void testGetCharacterById() throws ApiException {
        ComicsCharacter perso = CharacterExtractor.getCharacterById(apiClient, 60535);
        assertNotNull(perso);
        
        // Vérifier les valeurs de base
        assertEquals("Skrull Spider-Man", perso.getHeroName());
        assertEquals("https://comicvine.gamespot.com/a/uploads/original/3/31666/932012-skrull.jpg", perso.getPhotoUrl());
        assertEquals("Skrull that impersonated Spider-Man during Secret Invasion.", perso.getShortDescription());
        assertEquals(60535, perso.getId());
        
        // Vérifier les volumes
        assertEquals(Arrays.asList("X-Men"), perso.getVolumeCredits());
        
        // Vérifier les issues en ignorant les valeurs null
        List<String> expectedIssues = Arrays.asList(
            "Now I Know You're Human", "Secret Invasion", "", "", 
            "Book Three", "TPB", "", "Secret Invasion", "", 
            "TPB", "Brand New Secret Invasion", "Back From the Dead?!"
        );
        
        List<String> actualIssues = perso.getIssueCredits().stream()
            .filter(issue -> issue != null)
            .collect(Collectors.toList());
        
        assertEquals(expectedIssues, actualIssues, "Les issues devraient correspondre en ignorant les valeurs null");
        
        // Vérifier la description longue
        assertTrue(perso.getLongDescription().length() > 0);
    }
}