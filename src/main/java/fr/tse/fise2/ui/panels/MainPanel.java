package fr.tse.fise2.ui.panels;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatLightLaf;

import fr.tse.fise2.exception.ApiException;
import fr.tse.fise2.model.Comic;
import fr.tse.fise2.model.ComicManager;
import fr.tse.fise2.model.ComicsCharacter;
import fr.tse.fise2.service.SearchService;
import fr.tse.fise2.ui.LoginFrame;
import fr.tse.fise2.ui.LogoutDialog;

/**
 * Panel principal de l'application
 * 
 * @version 2.0
 */
public class MainPanel extends JPanel implements MainView {
	private static final String WELCOM_LABEL_FONT_PATH = "/fonts/Heroes Legend.ttf";
	private static final String DEFAULT_FONT_PATH = "/fonts/AfacadFlux-ExtraBold.ttf";
	private static final String LOGIN_ICON_PATH = "/images/login_icon.png";
	private static final String BACKGROUND_IMAGE_PATH = "/images/background_cyberpunk.jpg";
	private static final String BADGE_ICON_PATH = "/images/badge_comicverse.png";
	private static final String SHOPPING_ICON_PATH = "/images/bouton_achat_normal.png";
	private static final String SHOPPING_ICON_HOVERED_PATH = "/images/bouton_achat_clicked.png";

	private boolean userConnected; // Vérifier si un utilisateur est connecté

	private static JPanel contentPanel;
	private JPanel upperPart;
	private JPanel navigationPanel;
	private static String currentPanel = "Home";
		
	public MainPanel() throws ApiException {
		// Charger le thème FlatLaf
		FlatLightLaf.setup();

		// Définir le layout du panel principal
		setLayout(new BorderLayout());

		// Zone de contenu pour l'onglet actuel
		contentPanel = createContentPanel();
		add(contentPanel, BorderLayout.CENTER);

		// Créer la partie supérieure du panneau principal
		upperPart = createUpperPart();
		add(upperPart, BorderLayout.NORTH);

		// Onglets de navigation sur la gauche
		navigationPanel = createNavigationPanel();
		add(navigationPanel, BorderLayout.WEST);
	}

	/**
	 * Créer la partie supérieure du panneau principal (barre de recherche, logo, etc.)
	 * @return JPanel contenant la partie supérieure
	 */
	private JPanel createUpperPart() {
		Image backgroundImage = new ImageIcon(getClass().getResource(BACKGROUND_IMAGE_PATH)).getImage();

		JPanel upperPart = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (backgroundImage != null) {
					// Dessiner l'image en arrière-plan, redimensionnée pour remplir tout le panneau
					g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
				}
			}
		};

		upperPart.setLayout(new BoxLayout(upperPart, BoxLayout.X_AXIS));	// Use BoxLayout for better control
		upperPart.setBackground(Color.LIGHT_GRAY);							// Set background color
		upperPart.setPreferredSize(new Dimension(800, 150));	// Set preferred size
		upperPart.setBorder(new EmptyBorder(10, 10, 10, 10));	// Add padding around the panel

		// Welcome label
		JLabel welcomeLabel = new JLabel("ComicVERSE");
		welcomeLabel.setForeground(Color.yellow);
		try {
			welcomeLabel.setFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream(WELCOM_LABEL_FONT_PATH)).deriveFont(64f));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			welcomeLabel.setFont(new Font("Arial", Font.BOLD, 64)); // Fallback font
		}
		welcomeLabel.setBorder(new EmptyBorder(25, 0, 0, 10)); // Add padding to the right of the label
		upperPart.add(welcomeLabel);

		// Add glue to push the search elements (and later the login elements) to the right
		upperPart.add(Box.createHorizontalGlue());

		// Search bar panel
		JPanel searchBarPanel = createSearchBarPanel();
		upperPart.add(searchBarPanel);

		// Ajouter le panneau "Achat" 
		upperPart.add(Box.createHorizontalStrut(10)); // Ajouter un espace entre les éléments
		JPanel achatPanel = createAchatPanel();
		if(!userConnected) {
			achatPanel.setVisible(false); // Masquer le panneau d'achat si l'utilisateur n'est pas connecté
		}
		upperPart.add(achatPanel);

		// Login panel
		if(userConnected) {
			JPanel badgePanel = createBadgeConnected();
			upperPart.add(badgePanel);
		} else {
			JPanel loginPanel = createLoginPanel();
			upperPart.add(loginPanel);
		}

		return upperPart;
	}

	// Méthode pour rafraîchir l'interface utilisateur après connexion/déconnexion
	public void refreshUpperPart() {
		// Mettre à jour le statut de connexion
		userConnected = LoginFrame.isUserLoggedIn();
		System.out.println("Statut de connexion mis à jour : " + userConnected);
	
		// Supprimer l'ancienne upperPart
		if (upperPart != null) {
			remove(upperPart);
		}
	
		// Recréer une nouvelle upperPart
		upperPart = createUpperPart();
	
		// Ajouter la nouvelle upperPart
		add(upperPart, BorderLayout.NORTH);
	
		// Revalider et repeindre
		revalidate();
		repaint();
	}

	// Fonction pour créer un bouton avec deux états (normal et hover)
	private JPanel createAchatPanel() {
		JPanel achatPanel = new JPanel();
		achatPanel.setLayout(new BoxLayout(achatPanel, BoxLayout.Y_AXIS));
		achatPanel.setOpaque(false); // Rendre le panneau transparent pour correspondre au parent
		achatPanel.setBorder(new EmptyBorder(10, 10, 10, 30)); // Ajouter des marges de 10px autour

		// Charger et redimensionner les icônes
		ImageIcon achatIconNormal = resizeIcon(new ImageIcon(getClass().getResource(SHOPPING_ICON_PATH)), 100, 100);
		ImageIcon achatIconHovered = resizeIcon(new ImageIcon(getClass().getResource(SHOPPING_ICON_HOVERED_PATH)), 120, 120);
	
		JLabel achatLogo = new JLabel(achatIconNormal);
		achatLogo.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrer l'image
		achatPanel.add(achatLogo);
	
		// Gestion des événements pour changer d'icône au survol
		achatLogo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				achatLogo.setIcon(achatIconHovered);
			}
	
			@Override
			public void mouseExited(MouseEvent e) {
				achatLogo.setIcon(achatIconNormal);
			}
	
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("Bouton d'achat cliqué !");
				CardLayout cardLayout=(CardLayout)contentPanel.getLayout();
				ShoppingPanel shoppingPanel=new ShoppingPanel();
				contentPanel.add(shoppingPanel,"wishList");
				cardLayout.show(contentPanel,"wishList");
				currentPanel = "wishList";
				
				// Logique pour gérer l'achat
			}
		});
		return achatPanel;
	}
	
	/**
	 * Méthode utilitaire pour redimensionner une icône.
	 */
	private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
		Image img = icon.getImage();
		Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new ImageIcon(resizedImage);
	}

	/**
	 * Créer un panneau de recherche avec des checkboxes pour les personnages et les comics
	 * @return
	 */
	private JPanel createSearchBarPanel() {
		JPanel searchBarPanel = new JPanel();
		searchBarPanel.setLayout(new BoxLayout(searchBarPanel, BoxLayout.X_AXIS));
		searchBarPanel.setOpaque(false); // Fond transparent
		searchBarPanel.setPreferredSize(new Dimension(400, 40)); // Réduire la largeur
		searchBarPanel.setMaximumSize(new Dimension(400, 40)); // Limiter la largeur
		searchBarPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 4)); // Bordure épaisse de 4px

		// Checkbox pour Characters
		JCheckBox charactersCheckbox = new JCheckBox();
		charactersCheckbox.setIcon(new ImageIcon(getClass().getResource("/images/characters.png"))); // Icône personnalisée
		charactersCheckbox.setSelectedIcon(new ImageIcon(getClass().getResource("/images/characters_selected.png"))); // Icône sélectionnée
		charactersCheckbox.setOpaque(false);
		charactersCheckbox.setFocusPainted(false); // Supprimer le contour au focus
		charactersCheckbox.setContentAreaFilled(false); // Supprimer le fond de la checkbox
		charactersCheckbox.setBorderPainted(false); // Supprimer la bordure de la checkbox
		charactersCheckbox.setToolTipText("Search Characters"); // Info-bulle pour accessibilité
		charactersCheckbox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Curseur main

		// Checkbox pour Comics
		JCheckBox comicsCheckbox = new JCheckBox();
		comicsCheckbox.setIcon(new ImageIcon(getClass().getResource("/images/comics.png"))); // Icône personnalisée
		comicsCheckbox.setSelectedIcon(new ImageIcon(getClass().getResource("/images/comics_selected.png"))); // Icône sélectionnée
		comicsCheckbox.setSelected(true); // Sélection par défaut
		comicsCheckbox.setOpaque(false);
		comicsCheckbox.setFocusPainted(false); // Supprimer le contour au focus
		comicsCheckbox.setContentAreaFilled(false); // Supprimer le fond de la checkbox
		comicsCheckbox.setBorderPainted(false); // Supprimer la bordure de la checkbox
		comicsCheckbox.setToolTipText("Search Comics"); // Info-bulle pour accessibilité
		comicsCheckbox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Curseur main

		// Gérer la mutualité des checkboxes
		charactersCheckbox.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				comicsCheckbox.setSelected(false);
			}
		});

		comicsCheckbox.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				charactersCheckbox.setSelected(false);
			}
		});

		searchBarPanel.add(Box.createHorizontalStrut(10)); // Espacement gauche
		searchBarPanel.add(comicsCheckbox);
		searchBarPanel.add(Box.createHorizontalStrut(5)); // Espacement entre les checkboxes
		searchBarPanel.add(charactersCheckbox);
		searchBarPanel.add(Box.createHorizontalStrut(5)); // Espacement entre les checkboxes
	
		// Champ de recherche
		JTextField searchBar = new JTextField();
		searchBar.setBorder(null); // Retirer les bordures internes
		searchBar.setOpaque(false); // Fond transparent
		searchBar.setForeground(Color.WHITE); // Texte gris
		searchBar.setCaretColor(Color.WHITE); // Curseur blanc
		searchBar.setFont(new Font("Arial", Font.PLAIN, 16)); // Police simple
		searchBar.setPreferredSize(new Dimension(350, 30)); // Ajuster la largeur
		searchBar.setMaximumSize(new Dimension(350, 30)); // Limiter la largeur
		String placeholder = "Search here...";
		searchBar.setText(placeholder);
		searchBar.addActionListener(l -> {
			String query = searchBar.getText();
			String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
			if (!query.isEmpty()) {
				if (charactersCheckbox.isSelected()) {
					try {
						SearchService searchService = new SearchService();
						List<ComicsCharacter> characters = searchService.searchCharacters(encodedQuery);
						contentPanel.add(createCharactersSearchResultsPanel(characters), "Character Search Results");
						CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
						cardLayout.show(contentPanel, "Character Search Results");
						currentPanel = "Character Search Results";
					} catch (ApiException ex) {
						showError("Failed to search comics: " + ex.getMessage());
					}
				} else {
					try {
						SearchService searchService = new SearchService();
						List<Comic> comics = searchService.searchComics(encodedQuery);
						contentPanel.add(createComicsSearchResultsPanel(comics), "Comic Search Results");
						CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
						cardLayout.show(contentPanel, "Comic Search Results");
						currentPanel = "Comic Search Results";
					} catch (ApiException ex) {
						showError("Failed to search comics: " + ex.getMessage());
					}
				}
			}
		});
		searchBar.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (searchBar.getText().equals(placeholder)) {
					searchBar.setText("");
					searchBar.setForeground(Color.WHITE); // Texte blanc lors de la saisie
				}
			}
	
			@Override
			public void focusLost(FocusEvent e) {
				if (searchBar.getText().isEmpty()) {
					searchBar.setForeground(Color.GRAY); // Texte gris pour le placeholder
					searchBar.setText(placeholder);
				} else {
					searchBar.setForeground(Color.WHITE); // Garder le texte blanc si non vide
				}
			}
		});
	
		searchBarPanel.add(Box.createHorizontalStrut(10)); // Espacement gauche
		searchBarPanel.add(searchBar);
	
		// Bouton de recherche avec une icône
		ImageIcon normalIcon = resizeIcon(new ImageIcon(getClass().getResource("/images/loupe_normal.png")), 30, 30);
		ImageIcon clickedIcon = resizeIcon(new ImageIcon(getClass().getResource("/images/loupe_clicked.png")), 30, 30);
		
		JButton searchButton = new JButton(normalIcon);
		searchButton.setContentAreaFilled(false); // Retirer le fond
		searchButton.setBorder(null); // Pas de bordure
		searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Curseur main
		searchButton.setPreferredSize(new Dimension(30, 30)); // Taille ajustée
	
		// Action de recherche
		searchButton.addActionListener(e -> {
			String query = searchBar.getText().trim();
			if (!query.isEmpty()) {
				if (charactersCheckbox.isSelected()) {
					try {
						SearchService searchService = new SearchService();
						List<ComicsCharacter> characters = searchService.searchCharacters(query);
						contentPanel.add(createCharactersSearchResultsPanel(characters), "Character Search Results");
						CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
						cardLayout.show(contentPanel, "Character Search Results");
						currentPanel = "Character Search Results";
					} catch (ApiException ex) {
						showError("Failed to search comics: " + ex.getMessage());
					}
				} else {
					try {
						SearchService searchService = new SearchService();
						List<Comic> comics = searchService.searchComics(query);
						contentPanel.add(createComicsSearchResultsPanel(comics), "Comic Search Results");
						CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
						cardLayout.show(contentPanel, "Comic Search Results");
						currentPanel = "Comic Search Results";
					} catch (ApiException ex) {
						showError("Failed to search comics: " + ex.getMessage());
					}
				}
				
			}
		});
	
		// Ajouter des événements pour changer l'icône lors des interactions
		searchButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				searchButton.setIcon(clickedIcon); // Changer pour l'icône survolée
			}
	
			@Override
			public void mouseExited(MouseEvent e) {
				searchButton.setIcon(normalIcon); // Revenir à l'icône normale
			}
	
			@Override
			public void mousePressed(MouseEvent e) {
				searchButton.setContentAreaFilled(true);
				searchButton.setBackground(Color.LIGHT_GRAY); // Fond gris temporaire
			}
	
			@Override
			public void mouseReleased(MouseEvent e) {
				searchButton.setContentAreaFilled(false); // Restaurer l'état original
			}
		});
	
		searchBarPanel.add(searchButton);
		searchBarPanel.add(Box.createHorizontalStrut(2)); // Espacement droit
	
		return searchBarPanel;
	}

	/**
	 * Créer un panel pour le login
	 * @return JPanel contenant le logo et le texte de connexion
	 */
	private JPanel createLoginPanel() {
		JPanel loginPanel = new JPanel();
		loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
		loginPanel.setOpaque(false); // Rendre transparent pour correspondre au parent
		loginPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Ajouter des marges autour
	
		// Charger et redimensionner l'icône
		ImageIcon loginIconNormal = resizeIcon(new ImageIcon(getClass().getResource(LOGIN_ICON_PATH)), 95, 95);
		ImageIcon loginIconHovered = resizeIcon(new ImageIcon(getClass().getResource(LOGIN_ICON_PATH)), 120, 120); // Taille agrandie au survol
	
		JLabel loginLogo = new JLabel(loginIconNormal);
		loginLogo.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrer l'image
		loginLogo.setFocusable(true);
		loginPanel.add(loginLogo);
	
		// Gestion des événements pour agrandir l'image au survol
		loginLogo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				loginLogo.setIcon(loginIconHovered); // Image agrandie
			}
	
			@Override
			public void mouseExited(MouseEvent e) {
				loginLogo.setIcon(loginIconNormal); // Retour à la taille normale
			}
	
			@Override
			public void mouseClicked(MouseEvent e) {
				LoginFrame loginFrame = new LoginFrame(MainPanel.this);
				loginFrame.setVisible(true);
			}
		});
	
		// Texte sous l'image
		JLabel signInLabel = new JLabel("LOG IN");
		signInLabel.setForeground(Color.WHITE);
		signInLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrer le texte
		try {
			signInLabel.setFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream(DEFAULT_FONT_PATH)).deriveFont(16f));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			signInLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Police de secours
		}
		signInLabel.setBorder(new EmptyBorder(5, 0, 0, 0)); // Ajouter un espacement au-dessus du texte
		loginPanel.add(signInLabel);
	
		return loginPanel;
	}

	private JPanel createBadgeConnected() {
		JPanel badgePanel = new JPanel();
		badgePanel.setLayout(new BoxLayout(badgePanel, BoxLayout.X_AXIS));
		badgePanel.setOpaque(false);
		badgePanel.setBorder(new EmptyBorder(5, 2, 5, 5));

		ImageIcon badgeIcon = resizeIcon(new ImageIcon(getClass().getResource(BADGE_ICON_PATH)), 250, 250);
		JLabel badgeLogo = new JLabel(badgeIcon);
		badgeLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// Ajouter un curseur pointeur et une infobulle
		badgeLogo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		badgeLogo.setToolTipText("Click to logout");

		// Ajouter le listener pour la déconnexion
		badgeLogo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Window window = SwingUtilities.getWindowAncestor(MainPanel.this);
				if (window instanceof JFrame) {
					LogoutDialog dialog = new LogoutDialog((JFrame) window);
					dialog.setVisible(true);
		
					if (dialog.isConfirmed()) {
						// Réinitialiser l'utilisateur connecté
						LoginFrame.loggedInUser = null;
						
						// Réinitialiser les listes
						ComicManager.resetAllLists();
						
						// Rediriger vers la page d'accueil
						CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
						cardLayout.show(contentPanel, "Home");
						currentPanel = "Home";
						
						// Rafraîchir l'interface
						refreshAfterLogin();
					}
				}
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				badgeLogo.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				badgeLogo.setBorder(null);
			}
		});

		badgePanel.add(badgeLogo);
		return badgePanel;
	}
	
	private JPanel createNavigationPanel() {
		JPanel navigationPanel = new JPanel();
		navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.Y_AXIS));
		navigationPanel.setBackground(Color.DARK_GRAY);
		navigationPanel.setPreferredSize(new Dimension(200, 600));

		// Create tab buttons
		JButton tab1Button = createTabButton("Home", new Color(93, 153, 43));		
		JButton tab2Button = createTabButton("For You", new Color(173, 137, 99));
		JButton tab3Button = createTabButton("My Library",new Color(244, 146, 34));
		JButton tab4Button = createTabButton("Favorites", new Color(183, 69, 168));

		JButton tab5Button = new JButton("Continue Reading");
		tab5Button.setAlignmentX(CENTER_ALIGNMENT);
		tab5Button.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE)); // Make the button fit the column width
		tab5Button.setBackground(new Color(35, 89, 86));
		tab5Button.setFocusPainted(false); // Remove the focus border
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream(DEFAULT_FONT_PATH)).deriveFont(23f);
			tab5Button.setFont(font);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			tab5Button.setFont(new Font("Arial", Font.BOLD, 18)); // Fallback font
		}

		navigationPanel.add(tab1Button);
		if (userConnected) {
			navigationPanel.add(tab5Button);
			navigationPanel.add(tab2Button);
			navigationPanel.add(tab3Button);
			navigationPanel.add(tab4Button);
		}

		// Add action listeners to the buttons to switch content
		CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
		tab1Button.addActionListener(e -> {
			cardLayout.show(contentPanel, "Home");
			currentPanel = "Home";
		});

		tab2Button.addActionListener(e -> {
			cardLayout.show(contentPanel, "For You");
			currentPanel = "For You";
		});

		tab3Button.addActionListener(e -> {
			cardLayout.show(contentPanel, "My Library");
			currentPanel = "My Library";
		});

		tab4Button.addActionListener(e -> {
			cardLayout.show(contentPanel, "Favorites");
			currentPanel = "Favorites";
		});

		tab5Button.addActionListener(e -> {
			cardLayout.show(contentPanel, "Continue Reading");
			currentPanel = "Continue Reading";
		});
		
		return navigationPanel;
	}

	public void refreshNavigationPanel() {
		// Mettre à jour le statut de connexion
		userConnected = LoginFrame.isUserLoggedIn();
		System.out.println("Statut de connexion mis à jour : " + userConnected);
	
		try {
			// Supprimer l'ancien panneau de navigation s'il existe
			if (navigationPanel != null) {
				remove(navigationPanel);
				System.out.println("Ancien panneau de navigation supprimé.");
			}
	
			// Recréer un nouveau panneau de navigation
			navigationPanel = createNavigationPanel();
			System.out.println("Nouveau panneau de navigation créé.");
	
			// Ajouter le nouveau panneau de navigation
			add(navigationPanel, BorderLayout.WEST);
			System.out.println("Nouveau panneau de navigation ajouté au layout.");
	
			// Revalider et repeindre
			revalidate();
			repaint();
			System.out.println("Panneau de navigation rafraîchi.");
		} catch (Exception e) {
			System.err.println("Erreur lors de la mise à jour du panneau de navigation : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Créer le panel de contenu principal
	 * @return 
	 */
	private JPanel createContentPanel() {
		contentPanel = new JPanel();
		contentPanel.setLayout(new CardLayout());
		contentPanel.setBackground(Color.WHITE);

		GeneralRecommandationPanel recommandation = new GeneralRecommandationPanel();
		contentPanel.add(recommandation, "Home"); // Anciennement "À la une"
		ForYouPanel forYouPanel = new ForYouPanel();
		contentPanel.add(forYouPanel, "For You"); // Anciennement "Pour vous"
		ContinueReadingPanel continueReadingPanel= new ContinueReadingPanel();
		contentPanel.add(continueReadingPanel, "Continue Reading"); // Anciennement "Reprendre votre lecture"
		LibraryPanel library= new LibraryPanel();
		contentPanel.add(library, "My Library"); // Anciennement "Ma bibliothèque"
		FavoritePanel favoritePanel = new FavoritePanel();
		contentPanel.add(favoritePanel, "Favorites"); // Anciennement "Favoris"
		ShoppingPanel shoppingPanel=new ShoppingPanel();
		contentPanel.add(shoppingPanel,"wishList");// Anciennement "Liste d'achat"
		contentPanel.add(new JLabel("Content for Search Results"), "Search Results"); // Anciennement "Résultat de la recherche"
		
		return contentPanel;
	}

	/**
	 * Créer un bouton d'onglet pour la navigation latérale
	 * @param text Texte du bouton
	 * @param color Couleur de fond du bouton
	 * @return JButton pour l'onglet
	 */
	private JButton createTabButton(String text, Color color) {
		JButton button = new JButton(text);
		button.setAlignmentX(CENTER_ALIGNMENT);
		button.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE)); // Make the button fit the column width
		button.setBackground(color);
		button.setFocusPainted(false); // Remove the focus border
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream(DEFAULT_FONT_PATH)).deriveFont(25f);
			button.setFont(font);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			button.setFont(new Font("Arial", Font.BOLD, 18)); // Fallback font
		}

		return button;
	}

	// Method to update the content panel in the center with a new panel (like CharacterGridPanel)
	@Override
	public void updateContentPanel(JPanel newPanel) {
		//contentPanel.removeAll(); 	// Remove all existing components
		CardLayout cardLayout=(CardLayout)contentPanel.getLayout();
		contentPanel.add(newPanel,newPanel.getClass().getSimpleName()); // Add the new panel
		cardLayout.show(contentPanel,newPanel.getClass().getSimpleName());
		contentPanel.revalidate();  // Revalidate the layout
		contentPanel.repaint();     // Redraw the panel
		
	}

	// Méthode pour afficher les résultats de recherche de characters
	@Override
	public JPanel createCharactersSearchResultsPanel(List<ComicsCharacter> characters) {
		JPanel searchResultsPanel = new CharacterGridPanel(characters);
		return searchResultsPanel;
	}

	// Méthode pour afficher les résultats de recherche de comics
	@Override
	public JPanel createComicsSearchResultsPanel(List<Comic> comics) {
		JPanel searchResultsPanel = new ComicGridPanel(comics);
		return searchResultsPanel;
	}

	// Méthode pour afficher des messages d'erreur
	@Override
	public void showError(String message) {
		JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
	}

	// Méthode pour rafraîchir le contenu après la connexion
	@Override
	public void refreshAfterLogin() {
		// Mise à jour de la partie supérieure (upperPart)
		refreshUpperPart(); // Appel à la méthode qui recrée la upperPart
		refreshNavigationPanel(); // Appel à la méthode qui recrée le panneau de navigation
		LibraryPanel.refreshAllPanels();//Appel à la méthode qui recrée le panel de la bibliothèque
		ShoppingPanel.refresh();
		ContinueReadingPanel.refreshComics();

		SwingUtilities.invokeLater(() -> {
			// Rafraîchir directement le panel actuel
			CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
			
			// Forcer le rafraîchissement de "À la une"
			for (Component comp : contentPanel.getComponents()) {
				if (comp instanceof GeneralRecommandationPanel) {
					((GeneralRecommandationPanel) comp).refreshPanel();
				}
				if (comp instanceof FavoritePanel) {
					FavoritePanel.loadFavorites();
				}
			}
			// Réafficher le panel actuel
			cardLayout.show(contentPanel, "À la une");
		});
	}

	// Getter pour le contentPanel
	public static JPanel getContentPanel() {
		return contentPanel;
	}

	// Getter pour le currentPanel
	public static String getCurrentPanel() {
		return currentPanel;
	}

	// Setters
	public static void setContentPanel(JPanel panel) {
		contentPanel = panel;
	}

    public static void setCurrentPanel(String panelName) {
        currentPanel = panelName;
    }
}