package fr.tse.fise2.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import org.mockito.MockedStatic;

import fr.tse.fise2.api.ApiClient;
import fr.tse.fise2.api.CharacterExtractor;
import fr.tse.fise2.exception.ApiException;

class CharacterTest {
    // Constants pour éviter la duplication
    private static final String HERO_NAME = "Spider-Man";
    private static final String REAL_NAME = "Peter Parker";
    private static final String ALIASES = "Spidey";
    private static final String PHOTO_URL = "http://photo-url.com";
    private static final String BIRTH_DATE = "12/08/1962";
    private static final String SHORT_DESCRIPTION = "A friendly neighborhood Spider-Man.";
    private static final String LONG_DESCRIPTION = "Spider-Man is a superhero created by writer Stan Lee and artist Steve Ditko.";
    private static final List<String> ISSUE_CREDIT = List.of("Amazing Fantasy #15", "The Amazing Spider-Man #1");
    private static final List<String> VOLUME_CREDIT = List.of("Amazing Fantasy #15", "The Amazing Spider-Man #1");
    private static final String PUBLISHER = "Marvel Comics";
    private static final String PUBLISHER_IMAGE = "http://publisher-image.com";
    private static final String ORIGIN = "Bitten by a radioactive spider.";
    private static final String API_DETAIL_URL = "http://marvel-api-url.com/spider-man";
    private static final int ID = 1;

    private ComicsCharacter testCharacter;

    @BeforeEach
    void setUp() {
        testCharacter = new ComicsCharacter(
            HERO_NAME, 
            REAL_NAME, 
            ALIASES, 
            PHOTO_URL, 
            BIRTH_DATE, 
            SHORT_DESCRIPTION, 
            LONG_DESCRIPTION, 
            ISSUE_CREDIT, 
            VOLUME_CREDIT,
            PUBLISHER, 
            PUBLISHER_IMAGE, 
            ORIGIN, 
            API_DETAIL_URL, 
            ID
        );
    }

    // Test pour vérifier que les getters renvoient les valeurs correctes
    @Test
    void testCharacterInfoGetters() {
        // Vérification des valeurs via les getters
        assertEquals(HERO_NAME, testCharacter.getHeroName());
        assertEquals(REAL_NAME, testCharacter.getRealName());
        assertEquals(ALIASES, testCharacter.getAliases());
        assertEquals(PHOTO_URL, testCharacter.getPhotoUrl());
        assertEquals(BIRTH_DATE, testCharacter.getBirthDate());
       assertEquals(SHORT_DESCRIPTION, testCharacter.getShortDescription());
        assertEquals(LONG_DESCRIPTION, testCharacter.getLongDescription());
        assertEquals(ISSUE_CREDIT, testCharacter.getIssueCredits());
        assertEquals(VOLUME_CREDIT, testCharacter.getVolumeCredits());
        assertEquals(PUBLISHER, testCharacter.getPublisherName());
        assertEquals(PUBLISHER_IMAGE, testCharacter.getPublisherImage());
        assertEquals(ORIGIN, testCharacter.getOrigin());
        assertEquals(API_DETAIL_URL, testCharacter.getApiDetailUrl());
        assertEquals(ID, testCharacter.getId());
    }

    // Test pour vérifier le comportement du toString() quand les valeurs sont non nulles
    @Test
    void testCharacterInfoToString() {
        String expectedToString = "Character Information:\n" +
            "----------------------\n" +
            "Hero Name: " + HERO_NAME + "\n" +
            "Real Name: " + REAL_NAME + "\n" +
            "Aliases: " + ALIASES + "\n" +
            "Photo URL: " + PHOTO_URL + "\n" +
            "Birth Date: " + BIRTH_DATE + "\n" +
            "Short Description: " + SHORT_DESCRIPTION + "\n" +
            "Long Description: " + LONG_DESCRIPTION + "\n" +
            "Issue Credits: " + String.join(", ", ISSUE_CREDIT) + "\n" +
            "Volume Credits: " + String.join(", ", VOLUME_CREDIT) + "\n" +
            "Publisher: " + PUBLISHER + "\n" +
            "Publisher Image URL: " + PUBLISHER_IMAGE + "\n" +
            "Origin: " + ORIGIN + "\n" +
            "API Detail URL: " + API_DETAIL_URL + "\n" +
            "ID: " + ID + "\n";

    assertEquals(expectedToString, testCharacter.toString());
    }

    // Test pour vérifier le comportement du toString() quand les valeurs sont nulles
    @Test
    void testCharacterInfoToStringWithNullValues() {
            // Création d'un objet ComicsCharacter avec des valeurs nulles ou par défaut
    ComicsCharacter character = new ComicsCharacter(
        null, // heroName
        null, // realName
        null, // aliases
        null, // photoUrl
        null, // birthDate
        null, // shortDescription
        null, // longDescription
        null, // issueCredit
        null,
        null, // publisherName
        null, // publisherImage
        null, // origin
        null, // apiDetailUrl
        -1     // id
);

String expectedToString = "Character Information:\n" +
        "----------------------\n" +
        "Hero Name: Unknown\n" +
        "Real Name: Unknown\n" +
        "Aliases: No aliases\n" +
        "Photo URL: No photo\n" +
        "Birth Date: Unknown\n" +
        "Short Description: No description\n" +
        "Long Description: No description\n" +
        "Issue Credits: None\n" +
        "Volume Credits: None\n" +
        "Publisher: Unknown\n" +
        "Publisher Image URL: No image\n" +
        "Origin: Unknown\n" +
        "API Detail URL: Unknown\n" +
        "ID: -1\n";

assertEquals(expectedToString, character.toString()); }

   
    // Test pour vérifier que la méthode toString() gère correctement les caractères avec des valeurs "vides"
    @Test
    void testCharacterInfoWithEmptyValues() {
        ComicsCharacter character = new ComicsCharacter(
            HERO_NAME, null, null, null,
            null, null, null,null,
            null, null,null,null,null,-1
        );
        String expectedToString = "Character Information:\n" +
        "----------------------\n" +
        "Hero Name: " + HERO_NAME + "\n" +
        "Real Name: Unknown\n" +
        "Aliases: No aliases\n" +
        "Photo URL: No photo\n" +
        "Birth Date: Unknown\n" +
        "Short Description: No description\n" +
        "Long Description: No description\n" +
        "Issue Credits: None\n" +
        "Volume Credits: None\n" +
        "Publisher: Unknown\n" +
        "Publisher Image URL: No image\n" +
        "Origin: Unknown\n" +
        "API Detail URL: Unknown\n" +
        "ID: -1\n";

        assertEquals(expectedToString, character.toString());
    }

    @Test
    public void testCompleteCharacter() throws ApiException {
        // Création du personnage incomplet avec les informations de base
        ComicsCharacter incompleteCharacter = new ComicsCharacter(
            "Nightwing", // Hero Name
            null,        // Real Name (incomplet)
            null,        // Aliases (incomplet)
            "https://comicvine.gamespot.com/a/uploads/original/11117/111175090/9509240-5138111000-latest.jpg", // Photo URL
            null,        // Birth Date (incomplet)
            "Son of Kryptonian Skyfather Rao. Nightwing the god of darkness was tasked with hunting down evils which hid from Rao in the shadows.", // Short Description
            null,        // Long Description (incomplet)
            null,        // Issue Credits (incomplet)
            null,        // Volume Credits (incomplet)
            null,        // Publisher Name (incomplet)
            null,        // Publisher Image (incomplet)
            null,        // Origin (incomplet)
            null,        // API Detail URL (incomplet)
            191414       // ID
        );

        // Données détaillées que l'on s'attend à obtenir lors de l'appel à getCharacterById pour l'ID 191414
        ComicsCharacter detailedCharacter = new ComicsCharacter(
            "Nightwing", // Hero Name
            "Dick Grayson", // Real Name
            "Robin, The Dark Knight's protege", // Aliases
            "https://comicvine.gamespot.com/a/uploads/original/11117/111175090/9509240-5138111000-latest.jpg", // Photo URL
            "March 11, 1994", // Birth Date
            "Son of Kryptonian Skyfather Rao. Nightwing the god of darkness was tasked with hunting down evils which hid from Rao in the shadows.",
            "Nightwing, the god of darkness who seeks to hunt down evils that lurk in the shadows.", // Long Description
            Arrays.asList("Detective Comics #38", "Nightwing Vol. 1"), // Issue Credits
            Arrays.asList("Nightwing Vol. 1", "Batman and Robin Vol. 1"), // Volume Credits
            "DC Comics", // Publisher Name
            "https://comicvine.gamespot.com/a/uploads/remote/11117/111175090/9509240-5138111000-latest.jpg", // Publisher Image
            "Gotham City", // Origin
            "https://comicvine.gamespot.com/characters/dick-grayson-nightwing/", // API Detail URL
            191414 // ID
        );

        // Mock de l'appel à l'API pour récupérer les détails du personnage par ID
        try (MockedStatic<CharacterExtractor> mockedExtractor = mockStatic(CharacterExtractor.class)) {
            // Simuler l'appel de getCharacterById pour l'ID 191414
            mockedExtractor.when(() -> CharacterExtractor.getCharacterById(any(ApiClient.class), eq(191414)))
                        .thenReturn(detailedCharacter);

            // Avant l'appel de completeCharacter, vérifier que les informations sont incomplètes
            assertNull(incompleteCharacter.getRealName());
            assertNull(incompleteCharacter.getAliases());
            assertNull(incompleteCharacter.getBirthDate());
            assertEquals("Nightwing", incompleteCharacter.getHeroName());
            assertEquals("Son of Kryptonian Skyfather Rao. Nightwing the god of darkness was tasked with hunting down evils which hid from Rao in the shadows.", incompleteCharacter.getShortDescription());

            ApiClient apiClient = new ApiClient();
                        // Appel à la méthode completeCharacter pour compléter les informations du personnage
                        incompleteCharacter.completeCharacter(apiClient);

            // Après l'appel à completeCharacter, vérifier que les informations ont été correctement mises à jour
            assertEquals("Dick Grayson", incompleteCharacter.getRealName());
            assertEquals("Robin, The Dark Knight's protege", incompleteCharacter.getAliases());
            assertEquals("March 11, 1994", incompleteCharacter.getBirthDate());
            assertEquals("Nightwing, the god of darkness who seeks to hunt down evils that lurk in the shadows.", incompleteCharacter.getLongDescription());
            assertEquals(Arrays.asList("Detective Comics #38", "Nightwing Vol. 1"), incompleteCharacter.getIssueCredits());
            assertEquals(Arrays.asList("Nightwing Vol. 1", "Batman and Robin Vol. 1"), incompleteCharacter.getVolumeCredits());
            assertEquals("DC Comics", incompleteCharacter.getPublisherName());
            assertEquals("https://comicvine.gamespot.com/a/uploads/remote/11117/111175090/9509240-5138111000-latest.jpg", incompleteCharacter.getPublisherImage());
            assertEquals("Gotham City", incompleteCharacter.getOrigin());
            assertEquals("https://comicvine.gamespot.com/characters/dick-grayson-nightwing/", incompleteCharacter.getApiDetailUrl());
        }
    }

}
