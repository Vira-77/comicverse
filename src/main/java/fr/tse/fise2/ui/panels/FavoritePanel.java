package fr.tse.fise2.ui.panels;

import fr.tse.fise2.model.Comic;
import fr.tse.fise2.model.ComicManager;
import fr.tse.fise2.ui.components.ComicCard;
import fr.tse.fise2.ui.components.CustomScrollBarUI;
import fr.tse.fise2.controller.FavoriteController;

import java.awt.*;
import javax.swing.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Classe FavoritePanel
 * Cette classe représente un panneau qui affiche les comics ajoutés aux favoris sous forme de vignettes..
 * Elle permet d'ajouter, de supprimer et de filtrer les comics favoris.
 * 
 * @version 2.0
 * @author Lauriane TUTALAMO et Samuel PLET
 */
public class FavoritePanel extends JPanel {

	private static JPanel favoritesGridPanel;        // Panneau pour afficher les comics favoris
	private final JTextField searchField;           // Barre de recherche pour filtrer les comics favoris
	private static List<Comic> favorites;              	// Liste des comics favoris
	private static FavoriteController controller = new FavoriteController();    // Contrôleur pour gérer les favoris
												
	public FavoritePanel() {
		FavoritePanel.controller = new FavoriteController(); // Initialiser le contrôleur
		setLayout(new BorderLayout());
	
		// Panneau principal contenant les vignettes de comics
		favoritesGridPanel = new JPanel();
		favoritesGridPanel.setLayout(new GridLayout(0, 5, 10, 10)); // Grid avec 3 colonnes
		favoritesGridPanel.setBackground(Color.BLACK);
		
		// Ajouter le panneau de favoris dans un JScrollPane pour permettre le défilement
		JScrollPane scrollPane = CustomScrollBarUI.createScrollPane(favoritesGridPanel);
		add(scrollPane, BorderLayout.CENTER);

		// Barre de recherche
		// TODO: Modifier la barre de recherche pour qu'elle soit plus intuitive et plus efficace et si possible plus attrayante
		// TODO: Utiliser le LoadingPanel pour le temps de chargement
		JPanel searchPanel = new JPanel(new BorderLayout());
		searchField = new JTextField();
		searchField.setToolTipText("Search for comics by name"); // Info-bulle pour la barre de recherche
		searchField.addCaretListener(e -> filterFavorites()); // Filtrer les favoris lorsque l'utilisateur tape
		searchPanel.add(searchField, BorderLayout.CENTER);

		
			// Bouton reset/refresh (permet de mettre à jour le panel de favoris et de réinitialiser la recherche)
			JButton resetButton = new JButton("Reset");
			resetButton.addActionListener(e -> resetSearch()); // Réinitialiser la recherche
			searchPanel.add(resetButton, BorderLayout.EAST);
		

		add(searchPanel, BorderLayout.NORTH);

		// Charger les favoris à l'initialisation du panneau
		loadFavorites();
	}
			
	/**
	 * Charger les comics favoris et les afficher dans le panneau.
	 * Cette méthode est appelée à l'initialisation du panneau et après chaque ajout/suppression de favoris.
	 */
	public static void loadFavorites() {
		// Vider le panneau avant de recharger les favoris
		favoritesGridPanel.removeAll();

		// Obtenir la liste des comics favoris
		favorites = ComicManager.getFavoriteComics();

		// Créer une vignette pour chaque comic dans la liste des favoris
		for (Comic comic : favorites) {
			ComicCard comicTile = new ComicCard(comic);
			favoritesGridPanel.add(comicTile);  // Ajouter la vignette au panneau
		}
		
		// Réactualiser l'affichage
		favoritesGridPanel.revalidate();
		favoritesGridPanel.repaint();
	}

	/**
	 * Méthode pour ajouter un comic aux favoris et mettre à jour l'affichage.
	 * Cette méthode appelle le contrôleur pour ajouter le comic aux favoris.
	 * @param comic Le comic à ajouter aux favoris.
	 */
	public void addComicToFavorites(Comic comic) {
		controller.addFavorite(comic);
		loadFavorites();    // Recharge les favoris après l'ajout
	}

	/**
	 * Méthode pour supprimer un comic des favoris et mettre à jour l'affichage.
	 * Cette méthode appelle le contrôleur pour supprimer le comic des favoris.
	 * @param comic Le comic à supprimer des favoris.
	 */
	public void filterFavorites() {
		// Obtenir le texte de recherche
		String searchText = searchField.getText().toLowerCase();

		// Filtrer les favoris
		List<Comic> filteredFavorites = controller.filterFavorites(searchText);

		// Créer une liste des IDs des favoris filtrés
		Set<Integer> filteredIds = filteredFavorites.stream()
												.map(Comic::getId)
												.collect(Collectors.toSet());

		// Parcourir les cartes existantes pour identifier les non-filtrées
		for (Component component : favoritesGridPanel.getComponents()) {
			if (component instanceof ComicCard) {
				ComicCard card = (ComicCard) component;
				if (!filteredIds.contains(card.getComic().getId())) {
					FavoritePanel.removeComicCard(card.getComic()); // Suppression via méthode dédiée
				}
			}
		}

		// Rafraîchir le panneau
		favoritesGridPanel.revalidate();
		favoritesGridPanel.repaint();
	}

	/**
	 * Réinitialiser la recherche et afficher tous les comics favoris.
	 * Cette méthode est appelée lorsque l'utilisateur clique sur le bouton "Reset".
	 */
	public void resetSearch() {
		// Réinitialiser le champ de recherche
		searchField.setText("");
	
		// Obtenir la liste complète des favoris
		List<Comic> allFavorites =ComicManager.getFavoriteComics();
		System.out.println("Order of favorites après reset: " + allFavorites.stream()
														.map(Comic::getName) // Utilise le nom au lieu de l'ID
														.collect(Collectors.toList()));

	
		// Vider le panneau des cartes existantes
		favoritesGridPanel.removeAll();
	
		// Ajouter toutes les cartes correspondant aux favoris
		for (Comic comic : allFavorites) {
			
			FavoritePanel.addComicCard(comic);
			System.out.println("Comic card ajouté après reset pour: " + comic.getName());

	
		}
	
		// Rafraîchir le panneau pour afficher les cartes
		favoritesGridPanel.revalidate();
		favoritesGridPanel.repaint();
	}
	
	public List<Comic> getFavorites(){
		return favorites;
	}

	public JTextField getSearchField(){
		return searchField;
	}

	public static JPanel getFavoritesGridPanel() {
		return favoritesGridPanel;
	}

	public static void addComicCard(Comic comic) {
		ComicCard comicTile = new ComicCard(comic);
		favoritesGridPanel.add(comicTile);
		favoritesGridPanel.revalidate();
		favoritesGridPanel.repaint();
	}

	public static void removeComicCard(Comic comic) {
		for (Component component : favoritesGridPanel.getComponents()) {
			if (component instanceof ComicCard) {
				ComicCard comicCard = (ComicCard) component;
				if (comicCard.getComic().equals(comic)) {
					favoritesGridPanel.remove(comicCard);
					favoritesGridPanel.revalidate();
					favoritesGridPanel.repaint();
					break;
				}
			}
		}
	}
}