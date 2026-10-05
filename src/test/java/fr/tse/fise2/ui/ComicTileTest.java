package fr.tse.fise2.ui;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.tse.fise2.model.Comic;
import fr.tse.fise2.model.User;
import fr.tse.fise2.ui.components.ComicCard;

class ComicTileTest {

    @BeforeEach
    void setUp() {
        LoginFrame.loggedInUser = null; // Reset état utilisateur
    }
    
    @Test
    void shouldCreateComicTileWithoutUser() {
        Comic comic = new Comic("Test Comic", "", List.of(), "", List.of(), null, "", "", "",5);
        ComicCard tile = new ComicCard(comic);
        
        assertNotNull(tile);
        assertFalse(tile.hasUserButtons(), "Ne devrait pas avoir de boutons utilisateur");
    }
    
    @Test
    void shouldCreateComicTileWithUser() {
        User testUser = new User("testUser", "password");
        LoginFrame.loggedInUser = testUser;
        Comic comic = new Comic("Test Comic", "", List.of(), "", List.of(),null, "", "", "",4);
        
        ComicCard tile = new ComicCard(comic);
        
        assertTrue(tile.hasUserButtons(), "Devrait avoir des boutons utilisateur");
    }
    
    @Test
    void shouldDisplayComicName() {
        String expectedName = "Test Comic";
        Comic comic = new Comic(expectedName, "", List.of(), "", List.of(),null, "", "", "",9);
        
        ComicCard tile = new ComicCard(comic);
        
        assertEquals(expectedName, tile.getComic().getName());
        assertEquals(expectedName, tile.getNameButton().getText());
    }
}