package fr.tse.fise2.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.tse.fise2.ui.LoginFrame;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

public class FavoriteTest {

    private Comic comic1;
    private Comic comic2;

    @BeforeEach
    public void setUp() {
        //Création d'un nouvel utilisateur pour les tests
        LoginFrame.loggedInUser = new User(1, "test", "test", LocalDateTime.now());

        // Initialiser les comics pour les tests
        comic1 = new Comic("Comic 1", "url1", null, "url1",null, null, "deck1", "description1", "2024-12-01",7);
        comic2 = new Comic("Comic 2", "url2", null, "url2", null,null,"deck2", "description2", "2024-12-02",6);

        // Réinitialiser les favoris avant chaque test
        Favorite.resetFavorites();
    }

    @Test
    public void testAddComicToFavorites() {
        // Ajouter comic1 aux favoris
        Favorite.addComicToFavorites(comic1);
        assertTrue(Favorite.isFavorite(comic1)); // Vérifie que comic1 est bien dans les favoris
        assertEquals(1, Favorite.getFavorites().size()); // Vérifie que la liste des favoris contient 1 comic
    }

    @Test
    public void testRemoveComicFromFavorites() {
        // Ajouter comic1 aux favoris
        Favorite.addComicToFavorites(comic1);
        // Retirer comic1 des favoris
        Favorite.removeComicFromFavorites(comic1);
        assertFalse(Favorite.isFavorite(comic1)); // Vérifie que comic1 n'est plus dans les favoris
        assertEquals(0, Favorite.getFavorites().size()); // Vérifie que la liste des favoris est vide
    }

    @Test
    public void testIsFavorite() {
        // Ajouter comic1 aux favoris
        Favorite.addComicToFavorites(comic1);
        assertTrue(Favorite.isFavorite(comic1)); // Vérifie que comic1 est bien dans les favoris
        assertFalse(Favorite.isFavorite(comic2)); // Vérifie que comic2 n'est pas dans les favoris
    }

    @Test
    public void testResetFavorites() {
        // Ajouter plusieurs comics aux favoris
        Favorite.addComicToFavorites(comic1);
        Favorite.addComicToFavorites(comic2);
        assertEquals(2, Favorite.getFavorites().size()); // Vérifie que la liste contient 2 comics

        // Réinitialiser les favoris
        Favorite.resetFavorites();
        assertEquals(0, Favorite.getFavorites().size()); // Vérifie que la liste est vide après la réinitialisation
    }
}
