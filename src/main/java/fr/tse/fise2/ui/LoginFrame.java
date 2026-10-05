package fr.tse.fise2.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.border.EmptyBorder;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.*;

import fr.tse.fise2.db.DatabaseManager;
import fr.tse.fise2.model.ComicManager;
import fr.tse.fise2.model.User;
import fr.tse.fise2.ui.panels.MainPanel;

/**
 * Fenêtre de connexion/inscription
 * Cette classe gère l'interface graphique pour la connexion et l'inscription des utilisateurs.
 * Elle utilise un CardLayout pour afficher les panneaux de connexion et d'inscription.
 * 
 * @version 1.3
 * @author Samuel PLET
 */
public class LoginFrame extends JFrame {
	// === Attributs ===
	// Logger
	private static final Logger LOGGER = Logger.getLogger(LoginFrame.class.getName());

	// Composants principaux
	private final CardLayout cardLayout;
	private final JPanel cards;
	private final DatabaseManager dbManager;
    private MainPanel mainPanel;
	public static User loggedInUser;

	// Composants Login
	private JTextField loginUsernameField;
	private JPasswordField loginPasswordField;
	private JLabel loginMessageLabel;

	// Composants Register
	private JLabel usernameRegisterLabel;
	private JLabel passwordRegisterLabel;
	private JLabel confirmPasswordLabel;
	private JTextField registerUsernameField;
	private JPasswordField registerPasswordField;
	private JPasswordField confirmPasswordField;
	private JLabel registerMessageLabel;
	private JButton registerButton;

	// === Constantes ===
	// Validation
	private static final int MIN_USERNAME_LENGTH = 3;
	private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>])(?=\\S+$).{8,}$";
	private static final String MSG_PASSWORD_INVALID = "<html>Password must contain at least:" +
		"<br>- 8 characters" +
		"<br>- 1 uppercase letter" +
		"<br>- 1 lowercase letter" +
		"<br>- 1 special character</html>";

	// Composantes UI
	private static final Color ERROR_COLOR = Color.RED;
	private static final Color SUCCESS_COLOR = Color.GREEN;
	private static final Color LABEL_COLOR = Color.WHITE;
	private static final Dimension WINDOW_SIZE = new Dimension(600, 400);
	private static final String BACKGROUND_IMAGE_PATH = "/images/login_background.png";
	private static final String LOGIN_ICON_PATH = "/images/login_icon.png";
	private static final String LABEL_FONT_PATH = "/fonts/Heroes Legend.ttf";
	private static final Insets FIELD_PADDING = new Insets(5, 5, 5, 5);
	private static final int FIELD_WIDTH = 20;
	private static final int REDIRECT_DELAY = 1500;

	// Messages
	private static final String MSG_FIELDS_EMPTY = "Please fill in all fields";
	private static final String MSG_PASSWORDS_MISMATCH = "Passwords do not match";
	private static final String MSG_LOGIN_SUCCESS = "Login successful!";
	private static final String MSG_REGISTER_SUCCESS = "Registration successful!";


	// === Initialisation ===
	// Initialisation du logger
	static {
		try {
			// Créez le dossier log s'il n'existe pas
			File logDir = new File("./logs");
			if (!logDir.exists()) {
				logDir.mkdir();
			}

			// Configurez le FileHandler pour enregistrer les logs dans un fichier
			FileHandler fileHandler = new FileHandler("./logs/login.log", true);
			fileHandler.setFormatter(new SimpleFormatter());
			LOGGER.addHandler(fileHandler);
			LOGGER.config("Logger initialisé avec succès.");
		} catch (IOException e) {
			LOGGER.severe("Erreur lors de la configuration du FileHandler : " + e.getMessage());
		}
	}

	// Constructeur
	public LoginFrame(MainPanel mainPanel) {
		// Configuration de la fenêtre
		setTitle("ComicVERSE - Log in/Sign up");
		setSize(WINDOW_SIZE);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);

        this.mainPanel = mainPanel;
        
		// Initialisation de la base de données et du CardLayout
		dbManager = new DatabaseManager();
		cardLayout = new CardLayout();
		cards = new JPanel(cardLayout);

		// Création des panneaux
		cards.add(createLoginPanel(), "login");
		cards.add(createRegisterPanel(), "register");
		cards.setOpaque(false);
		add(cards);

		// Créer et configurer le panel de fond
		BackgroundPanel backgroundPanel = new BackgroundPanel();
		backgroundPanel.setLayout(new BorderLayout());
		remove(cards);
		backgroundPanel.add(cards, BorderLayout.CENTER);

		// Définir comme contentPane
		setContentPane(backgroundPanel);

		// Afficher le login par défaut
		cardLayout.show(cards, "login");
	}

	// === Panneau de fond avec image ===
	private class BackgroundPanel extends JPanel {
		private Image backgroundImage;
	
		public BackgroundPanel() {
			// Charger l'image
			try {
				ImageIcon icon = new ImageIcon(getClass().getResource(BACKGROUND_IMAGE_PATH));
				backgroundImage = icon.getImage();
			} catch (Exception e) {
				LOGGER.severe("Erreur chargement image: " + e.getMessage());
			}
			setOpaque(true);
		}
	
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (backgroundImage != null) {
				// Dessiner l'image pour remplir tout le panel
				g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
			} else {
				// Fallback si pas d'image
				g.setColor(Color.GRAY);
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
			}
		}
	}

	// === Création des panneaux ===
	private JPanel createLoginPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
	
		// Créer les labels avec style
		JLabel userLabel = new JLabel("User Name:");
		userLabel.setBorder(new EmptyBorder(10, 0, 0, 0));
		JLabel passLabel = new JLabel("Password:");
		passLabel.setBorder(new EmptyBorder(10, 0, 0, 0));
		
		// Charger la police personnalisée
		try {
			Font customFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream(LABEL_FONT_PATH)).deriveFont(12f);
			styleLabel(userLabel, customFont);
			styleLabel(passLabel, customFont);
		} catch (Exception e) {
			LOGGER.warning("Police personnalisée non chargée, utilisation de la police par défaut");
			styleLabel(userLabel, new Font("Arial", Font.BOLD, 12));
			styleLabel(passLabel, new Font("Arial", Font.BOLD, 12));
		}
	
		// Ajouter l'icône de login
		try {
			ImageIcon loginIcon = new ImageIcon(getClass().getResource(LOGIN_ICON_PATH));
			JLabel iconLabel = new JLabel(loginIcon);
			addComponent(panel, iconLabel, 0, 0, 2);
		} catch (Exception e) {
			LOGGER.warning("Impossible de charger l'icône de login");
		}
	
		// Initialiser les champs une seule fois
		loginUsernameField = new JTextField(FIELD_WIDTH);
		loginPasswordField = new JPasswordField(FIELD_WIDTH);
		loginMessageLabel = new JLabel(" ");
		loginMessageLabel.setForeground(ERROR_COLOR);
	
		JButton loginButton = new JButton("Login");
		JButton showRegisterButton = new JButton("Create an account");

		// Définir le fond des boutons en noir
		loginButton.setBackground(Color.BLACK);
		showRegisterButton.setBackground(Color.BLACK);

		// Définir la couleur du texte des boutons en blanc
		loginButton.setForeground(Color.WHITE);
		showRegisterButton.setForeground(Color.WHITE);
	
		// Ajout des composants
		addComponent(panel, userLabel, 0, 1, 1);
		addComponent(panel, loginUsernameField, 1, 1, 1);
		addComponent(panel, passLabel, 0, 2, 1);
		addComponent(panel, loginPasswordField, 1, 2, 1);
		addComponent(panel, loginButton, 0, 3, 1);
		addComponent(panel, showRegisterButton, 1, 3, 1);
		addComponent(panel, loginMessageLabel, 0, 4, 2);
	
		loginButton.addActionListener(e -> handleLogin());
		showRegisterButton.addActionListener(e -> cardLayout.show(cards, "register"));
	
		return panel;
	}

	private JPanel createRegisterPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
	
		// Créer les labels avec style
		usernameRegisterLabel = new JLabel("User Name:");
		usernameRegisterLabel.setBorder(new EmptyBorder(10, 0, 0, 0));
		passwordRegisterLabel = new JLabel("Password:");
		passwordRegisterLabel.setBorder(new EmptyBorder(10, 0, 0, 0));
		confirmPasswordLabel = new JLabel("Confirm Password:");
		confirmPasswordLabel.setBorder(new EmptyBorder(10, 0, 0, 0));
	
		// Charger la police personnalisée
		try {
			Font customFont = Font.createFont(Font.TRUETYPE_FONT, 
				getClass().getResourceAsStream(LABEL_FONT_PATH)).deriveFont(12f);
			styleLabel(usernameRegisterLabel, customFont);
			styleLabel(passwordRegisterLabel, customFont);
			styleLabel(confirmPasswordLabel, customFont);
		} catch (Exception e) {
			Font defaultFont = new Font("Arial", Font.BOLD, 12);
			styleLabel(usernameRegisterLabel, defaultFont);
			styleLabel(passwordRegisterLabel, defaultFont);
			styleLabel(confirmPasswordLabel, defaultFont);
		}
	
		// Ajouter l'icône de login
		try {
			ImageIcon loginIcon = new ImageIcon(getClass().getResource(LOGIN_ICON_PATH));
			JLabel iconLabel = new JLabel(loginIcon);
			addComponent(panel, iconLabel, 0, 0, 2);
		} catch (Exception e) {
			LOGGER.warning("Impossible de charger l'icône de login");
		}
	
		registerUsernameField = new JTextField(FIELD_WIDTH);
		registerPasswordField = new JPasswordField(FIELD_WIDTH);
		confirmPasswordField = new JPasswordField(FIELD_WIDTH);
		registerMessageLabel = new JLabel(" ");
		registerMessageLabel.setForeground(ERROR_COLOR);
	
		registerButton = new JButton("Sign Up");
		JButton backToLoginButton = new JButton("Back to Login");

		// Définir le fond des boutons en noir
		registerButton.setBackground(Color.BLACK);
		backToLoginButton.setBackground(Color.BLACK);

		// Définir la couleur du texte des boutons en blanc
		registerButton.setForeground(Color.WHITE);
		backToLoginButton.setForeground(Color.WHITE);
			
		// Ajout des composants (décalés d'une ligne pour l'icône)
		addComponent(panel, usernameRegisterLabel, 0, 1, 1);
		addComponent(panel, registerUsernameField, 1, 1, 1);
		addComponent(panel, passwordRegisterLabel, 0, 2, 1);
		addComponent(panel, registerPasswordField, 1, 2, 1);
		addComponent(panel, confirmPasswordLabel, 0, 3, 1);
		addComponent(panel, confirmPasswordField, 1, 3, 1);
		addComponent(panel, registerButton, 0, 4, 1);
		addComponent(panel, backToLoginButton, 1, 4, 1);
		addComponent(panel, registerMessageLabel, 0, 5, 2);
	
		registerButton.addActionListener(e -> handleRegister());
		backToLoginButton.addActionListener(e -> cardLayout.show(cards, "login"));
	
		setupFieldValidation();
	
		return panel;
	}

	// === Gestion des événements ===
	private void handleLogin() {
		String username = loginUsernameField.getText();
		String password = new String(loginPasswordField.getPassword());

		if (username.isEmpty() || password.isEmpty()) {
			showError(loginMessageLabel, MSG_FIELDS_EMPTY);
			LOGGER.warning("Tentative de connexion avec des champs vides");
			return;
		}

		try {
			DatabaseManager.executeInTransaction(conn -> {
				if (dbManager.validateUser(conn, username, password)) {
					showSuccess(loginMessageLabel, MSG_LOGIN_SUCCESS);
					loginMessageLabel.setText("Login successful!");
					// Gérer la connexion réussie : Stocker l'utilisateur connecté
					User user = dbManager.getUserByUsername(conn, username);
					loggedInUser = user; 
					
					ComicManager.setComicsList(dbManager.getComicFromDatabase(conn, user));
					
							

                    // Mettre à jour la page d'accueil
                    if (mainPanel != null) {
                        mainPanel.refreshAfterLogin();
                    }
					dispose();
				} else {
					showError(loginMessageLabel, "Incorrect username or password");
					LOGGER.warning("Tentative de connexion avec des identifiants incorrects");
				}
			});
		} catch (SQLException ex) {
			LOGGER.severe("Erreur de connexion: " + ex.getMessage());
			showError(loginMessageLabel, "Erreur lors de la connexion: " + ex.getMessage());
		}
	}

	private void handleRegister() {
		String username = registerUsernameField.getText();
		String password = new String(registerPasswordField.getPassword());
		String confirmPassword = new String(confirmPasswordField.getPassword());

		if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
			showError(registerMessageLabel, MSG_FIELDS_EMPTY);
			LOGGER.warning("Tentative d'inscription avec des champs vides");
			return;
		}

		if (!password.equals(confirmPassword)) {
			showError(registerMessageLabel, MSG_PASSWORDS_MISMATCH);
			LOGGER.warning("Tentative d'inscription avec des mots de passe non identiques");
			return;
		}

		try {
			DatabaseManager.executeInTransaction(conn -> {
				User newUser = new User(username, password);
				if (!newUser.isValid()) {
					showError(registerMessageLabel, "Invalid username or password");
					LOGGER.warning("Tentative d'inscription avec des identifiants invalides");
					return;
				}

				dbManager.createUser(conn, newUser);
				showSuccess(registerMessageLabel, MSG_REGISTER_SUCCESS);
				LOGGER.info("Inscription réussie pour l'utilisateur: " + username);
				// Retour au login après un délai
			Timer timer = new Timer(REDIRECT_DELAY, e -> cardLayout.show(cards, "login"));
				timer.setRepeats(false);
				timer.start();
			});
		} catch (SQLException ex) {
			LOGGER.severe("Erreur d'inscription: " + ex.getMessage());
			showError(registerMessageLabel, "Error during registration: " + ex.getMessage());
		}
	}

	// === Validation des champs ===
	private class ValidationListener implements DocumentListener {
		public void changedUpdate(DocumentEvent e) { validateFields(); }
		public void insertUpdate(DocumentEvent e) { validateFields(); }
		public void removeUpdate(DocumentEvent e) { validateFields(); }
	}

	private void setupFieldValidation() {
		ValidationListener listener = new ValidationListener();
		registerUsernameField.getDocument().addDocumentListener(listener);
		registerPasswordField.getDocument().addDocumentListener(listener);
		confirmPasswordField.getDocument().addDocumentListener(listener);
	}

	private void validateFields() {
		String username = registerUsernameField.getText();
		String password = new String(registerPasswordField.getPassword());
		String confirmPassword = new String(confirmPasswordField.getPassword());
	
		if (username.length() < MIN_USERNAME_LENGTH) {
			showError(registerMessageLabel, String.format("Username must be at least %d characters long", MIN_USERNAME_LENGTH));
			registerButton.setEnabled(false);
			return;
		}
	
		if (!password.matches(PASSWORD_PATTERN)) {
			showError(registerMessageLabel, MSG_PASSWORD_INVALID);
			registerButton.setEnabled(false);
			return;
		}
	
		if (!password.equals(confirmPassword)) {
			showError(registerMessageLabel, MSG_PASSWORDS_MISMATCH);
			registerButton.setEnabled(false);
			return;
		}
	
		showSuccess(registerMessageLabel, "Form is valid");
		registerButton.setEnabled(true);
	}
	
	// === Utilitaires UI ===
	private GridBagConstraints createDefaultConstraints() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = FIELD_PADDING;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		return gbc;
	}

	private void addComponent(JPanel panel, Component component, int gridx, int gridy, int gridwidth) {
		GridBagConstraints gbc = createDefaultConstraints();
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.gridwidth = gridwidth;
		panel.add(component, gbc);
		}

		private void styleMessageLabel(JLabel label, String message, Color color) {
		// Si le message n'est pas déjà en HTML et contient des sauts de ligne
		if (!message.startsWith("<html>") && message.contains("\n")) {
			message = "<html>" + message.replace("\n", "<br>") + "</html>";
		}
		label.setText(message);
		label.setForeground(color);
		label.setFont(label.getFont().deriveFont(16f)); // Définir une taille de police fixe
		}

		private void showError(JLabel label, String message) {
		styleMessageLabel(label, message, ERROR_COLOR);
	}
	
	private void showSuccess(JLabel label, String message) {
		styleMessageLabel(label, message, SUCCESS_COLOR);
	}

	private void styleLabel(JLabel label, Font font) {
		label.setForeground(LABEL_COLOR);
		label.setFont(font);
	}

	// === Méthode pour obtenir l'utilisateur connecté ===
	public static User getLoggedInUser() {
		return loggedInUser;
	}

	public static String getLoggedInUsername() {
		// Retourne le nom d'utilisateur si un utilisateur est connecté, null sinon
		return loggedInUser != null ? loggedInUser.getUsername() : null; 
	}

    public static boolean isUserLoggedIn() {
        return loggedInUser != null; // Retourne true si un utilisateur est connecté, false sinon.
    }
}