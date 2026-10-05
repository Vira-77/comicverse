package fr.tse.fise2.ui;

import fr.tse.fise2.api.ApiClient;
import fr.tse.fise2.exception.CharacterDisplayException;
import fr.tse.fise2.model.ComicsCharacter;

import java.awt.*;
import javax.swing.*;
import java.net.URL;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import fr.tse.fise2.exception.ApiException;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

// TODO: Revoir ce qui doit être affiché et comment l'afficher (regardez la classe ComicsCharacter pour les informations disponibles et CharacterExtractor pour savoir comment les récupérer sinon demander à Lauriane)

/** 
 * This class is used to display the information of a character chosen by the user.
 * The JLabel displays the name of the character and the JTextArea displays the information of the character.
 * The information displayed are the name, the real name, the birth date, the aliases, the count of issue appeareance, the associated image, a mark and a description.
 * 
 * @author Luc SHANG
 */
public class CharactersInformation extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(CharactersInformation.class.getName());
    private static final int MAX_IMAGE_WIDTH = 500;
    private static final int MAX_IMAGE_HEIGHT = 600;
    
    private final ComicsCharacter character;
    private final JPanel rightPanel;
    private final JPanel leftPanel;
    private final JButton backButton = new JButton("Back");

    // Initialisation du logger
    static {
        try {
            // Créez le dossier log s'il n'existe pas
            File logDir = new File("./logs");
            if (!logDir.exists()) {
                logDir.mkdir();
            }

            // Configurez le FileHandler pour enregistrer les logs dans un fichier
            FileHandler fileHandler = new FileHandler("./logs/CharactersInformation.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.config("Logger initialisé avec succès.");
        } catch (IOException e) {
            LOGGER.severe("Erreur lors de la configuration du FileHandler : " + e.getMessage());
        }
    }
    
    /**
     * Constructeur de la classe CharactersInformation
     * Affiche les informations détaillées d'un personnage.
     * 
     * @param character les informations du personnage à afficher
     */    
    public CharactersInformation(ComicsCharacter character) throws ApiException {
        this.character = Objects.requireNonNull(character, "Character cannot be null");
        ApiClient apiClient = new ApiClient();
        try {
            this.character.completeCharacter(apiClient); // Complète les informations du personnage
            LOGGER.info("Création panneau pour: " + character.getHeroName());
        } catch (ApiException e) {
            LOGGER.severe("Error completing character information: " + e.getMessage());
        }

        // Mise en place du panel principal avec BorderLayout
        setLayout(new BorderLayout());
        this.rightPanel = createRightPanel();
        this.leftPanel = createLeftPanel();
        
        // JSplitPane pour séparer les deux panels
        configureSplitPane();
    }
    
    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(11, 19, 47));
        
        // Add backButton to the top right
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(11, 19, 47));
        backButton.setPreferredSize(new java.awt.Dimension(150, 60));
        backButton.setFont(new java.awt.Font("Impact", java.awt.Font.BOLD, 25)); // More visible bold font
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.NORTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);    // Marges entre les composants
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Add text components with black color
        JLabel nameLabel = new JLabel("<html><b><u> Name</u></b> : " + character.getHeroName() + "</html>" );
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new java.awt.Font("Arial BLACK", java.awt.Font.BOLD, 25));
        gbc.gridy = 0;
        gbc.weighty = 0.1;
        panel.add(nameLabel, gbc);

        JLabel realNameLabel = new JLabel("<html><b><u> Real Name</u></b> : " + character.getRealName() + "</html>");
        realNameLabel.setForeground(Color.WHITE);
        realNameLabel.setFont(new java.awt.Font("Arial BLACK", java.awt.Font.BOLD, 25));
        gbc.gridy = 1;
        panel.add(realNameLabel, gbc);

        JLabel birthLabel = new JLabel("<html><b><u> Birthdate</u></b> : " + character.getBirthDate() + "</html>");
        birthLabel.setFont(new java.awt.Font("Arial BLACK", java.awt.Font.BOLD, 25));
        birthLabel.setForeground(Color.WHITE);
        gbc.gridy = 2;
        panel.add(birthLabel, gbc);

        JLabel firstAppearanceLabel = new JLabel("<html><div style='white-space: nowrap;'><b><u> First Appearance</u></b> : " + character.getAliases() + "</div></html>");
        firstAppearanceLabel.setFont(new java.awt.Font("Arial BLACK", java.awt.Font.BOLD, 25));
        firstAppearanceLabel.setForeground(Color.WHITE);
        gbc.gridy = 3;
        panel.add(firstAppearanceLabel, gbc);

        JLabel issueCreditsLabel = new JLabel("<html><div style='white-space: nowrap;'><b><u> Issue Credits</u></b> :" + buildDisplayList(character.getIssueCredits()) + "</div></html>");
        issueCreditsLabel.setFont(new java.awt.Font("Arial BLACK", java.awt.Font.BOLD, 25));
        issueCreditsLabel.setForeground(Color.WHITE);
        gbc.gridy = 4;
        panel.add(issueCreditsLabel, gbc); 

        JLabel descLabel = new JLabel("<html><div style='width:100%;'><b><u> Description</u></b> : " + character.getShortDescription() + "</div></html>");
        descLabel.setFont(new java.awt.Font("Arial BLACK", java.awt.Font.BOLD, 25));
        descLabel.setForeground(Color.WHITE);
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(descLabel, gbc);



        return panel;
    }

    
    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout());  // Changed to BorderLayout
        panel.setBackground(new Color(11, 19, 47));

        
        try {
            JLabel imageLabel = loadImage(character.getPhotoUrl());
            imageLabel.setHorizontalAlignment(JLabel.CENTER);  // Center horizontally
            imageLabel.setVerticalAlignment(JLabel.CENTER);    // Center vertically
            panel.add(imageLabel, BorderLayout.CENTER);        // Add to CENTER of BorderLayout
        } catch (CharacterDisplayException e) {
            LOGGER.warning(e.getMessage());
            panel.add(createErrorLabel());
        }
        
        return panel;
    }
    
    private JLabel loadImage(String imageUrl) throws CharacterDisplayException {
        JLabel imageLabel = new JLabel();
        try {
            URL url = new URL(imageUrl);
            Image image = ImageIO.read(url);
            Image scaledImage = scaleImage(image);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        } catch (IOException e) {
            throw new CharacterDisplayException("Image loading failed", e);
        }
        return imageLabel;
    }
    
    private Image scaleImage(Image image) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        
        if (width > MAX_IMAGE_WIDTH || height > MAX_IMAGE_HEIGHT) {
            double scale = Math.min((double)MAX_IMAGE_WIDTH/width, 
                                  (double)MAX_IMAGE_HEIGHT/height);
            return image.getScaledInstance(
                (int)(width * scale),
                (int)(height * scale),
                Image.SCALE_SMOOTH
            );
        }
        
        return image;
    }
    
    private JLabel createErrorLabel() {
        JLabel label = new JLabel("Image not available");
        label.setForeground(Color.RED);
        return label;
    }
    
    private void configureSplitPane() {
        JSplitPane splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            leftPanel,
            rightPanel
        );
        splitPane.setDividerLocation(MAX_IMAGE_WIDTH);  // Position fixe du diviseur à la largeur max du leftPanel
        splitPane.setResizeWeight(0.5);             // 50% pour le panel gauche
        splitPane.setDividerSize(0);                // Cache le diviseur
        
        // Ajout du JSplitPane au panel principal
        add(splitPane, BorderLayout.CENTER);
    }

    public JButton getBackButton() {
        return backButton;
    }

    private String buildDisplayList(java.util.List<String> list) {
        if (list == null || list.isEmpty()) {
            return "Not specified by the ComicVine API";
        } else if (list.size() > 5) {
            return list.get(0) + ", " + list.get(1) + ", " + list.get(2) + ", " + list.get(3) + ", " + list.get(4) + ",... ";
        }
        return String.join(", ", list);
    }
}