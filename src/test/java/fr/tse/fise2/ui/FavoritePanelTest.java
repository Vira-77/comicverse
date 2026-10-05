package fr.tse.fise2.ui;

import fr.tse.fise2.model.User;
import fr.tse.fise2.ui.panels.FavoritePanel;
import fr.tse.fise2.model.Comic;
import fr.tse.fise2.model.ComicManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class FavoritePanelTest {

	private FavoritePanel favoritePanel;

	@BeforeEach
	public void setUp() {
		//Création d'un nouvel utilisateur pour les tests
 		LoginFrame.loggedInUser = new User(1, "test", "test", LocalDateTime.now());
		
		// Réinitialiser les favoris avant chaque test
		ComicManager.resetAllLists();
		favoritePanel = new FavoritePanel();
	}

	@AfterEach
	public void tearDown() {
		// Réinitialiser les favoris après chaque test
		ComicManager.resetAllLists();
		LoginFrame.loggedInUser = null;
	}

	@Test
	public void testAddComicToFavorites() {
		
		// Création d'un comic de test
		Comic comic = new Comic("Comic 1",  "https://example.com/supercomic1.jpg",
			Arrays.asList("Superhero1", "AdventurerX"),
			"https://example.com/api/comic1",
			Arrays.asList("Hero1", "Villain1", "Sidekick2"),
			null,
			"An exciting tale of a superhero's journey",
			"SuperComic Adventures is about a superhero who fights evil in a futuristic city...",
			"2023-10-01",7);

		// Ajouter le comic aux favoris
		favoritePanel.addComicToFavorites(comic);

		// Vérifier que le comic a bien été ajouté aux favoris
		List<Comic> favorites = favoritePanel.getFavorites();
		assertTrue(favorites.contains(comic), "Le comic devrait être dans la liste des favoris.");

		// Vérifier que le boolean inFavorite est à true 
		assertTrue(comic.getInFavorite(), "Le comic devrait être marqué comme favori.");
	}

	/**
		@Test
		public void testRemoveComicFromFavorites() {
			Comic comic = new Comic("Comic 1", "https://example.com/comic1.jpg", null,null, null, null, null, null, null,3);
			favoritePanel.addComicToFavorites(comic);
	
			// Vérifier que le comic a bien été ajouté
			assertTrue(favoritePanel.getFavorites().contains(comic));
	
			// Supprimer le comic des favoris
			favoritePanel.removeComicFromFavorites(comic);
	
			// Vérifier que le comic a bien été supprimé
			assertFalse(favoritePanel.getFavorites().contains(comic), "Le comic devrait être retiré des favoris.");
	
			// Vérifier que le boolean inFavorite est à false 
			assertFalse(comic.getInFavorite(), "Le comic ne devrait pas être marqué comme favori.");
		}
	*/

	@Test
	public void testAddComicToFavoritesViaComicManager() {
		// Arrange: Ajoutez quelques comics aux favoris
		Comic comic1 = new Comic("Comic 1", "https://example.com/comic1.jpg", null, null, null,null, null, null, null,9);
		Comic comic2 = new Comic("Comic 2", "https://example.com/comic2.jpg", null, null, null,null,null, null, null,78);
		
		ComicManager.addComicToFavorites(comic1);
		ComicManager.addComicToFavorites(comic2);

		// Assert: Vérifiez que le nombre de ComicTile dans le panneau est correct
		assertEquals(2, FavoritePanel.getFavoritesGridPanel().getComponentCount());
	}

	@Test
	public void testFilterFavorites() {
		// Arrange: Ajoutez quelques comics aux favoris
		Comic comic1 = new Comic("Comic 1",  "https://example.com/supercomic1.jpg",
			Arrays.asList("Superhero1", "AdventurerX"),
			"https://example.com/api/comic1",
			Arrays.asList("Hero1", "Villain1", "Sidekick2"),
			null,
			"An exciting tale of a superhero's journey",
			"SuperComic Adventures is about a superhero who fights evil in a futuristic city...",
			"2023-10-01",456);

		Comic comic2 = new Comic("Comic 2", "https://example.com/mightyleague2.jpg",
			Arrays.asList("MightyHero", "Captain Victory"),
			"https://example.com/api/comic2",
			Arrays.asList("Champion1", "Villainess", "Supporter3"),
			null,
			"The greatest heroes of all time unite to stop a new threat",
			"Mighty League of Heroes follows the formation of a team of heroes working together to defeat a powerful enemy...",
			"2023-11-15",754);

		Comic comic3 = new Comic("Special Comic","https://example.com/chronicles3.jpg",
			Arrays.asList("KingdomExplorer", "The Last Monarch"),
			"https://example.com/api/comic3",
			Arrays.asList("ExplorerX", "MysteriousSorcerer"),
			null,
			"An epic tale of a forgotten kingdom and its rise from the ashes",
			"Chronicles of the Lost Kingdom unveils the history of an ancient, forgotten kingdom and its quest to reclaim its former glory...",
			"2024-01-30",678);

			ComicManager.addComicToFavorites(comic1);
			ComicManager.addComicToFavorites(comic2);
			ComicManager.addComicToFavorites(comic3);

		// Créez le panneau des favoris
		FavoritePanel favoritePanel = new FavoritePanel();

		

		// Assert: Vérifiez que tous les favoris sont chargés initialement
		assertEquals(3, FavoritePanel.getFavoritesGridPanel().getComponentCount());

		// Act: Appliquez un filtre
		favoritePanel.getSearchField().setText("Special");
		favoritePanel.filterFavorites();

		// Assert: Vérifiez que le nombre de ComicTile affichés après filtrage est correct
		assertEquals(1, FavoritePanel.getFavoritesGridPanel().getComponentCount());
	}
}