package fr.tse.fise2.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import fr.tse.fise2.exception.CharacterDisplayException;
import fr.tse.fise2.model.Comic;
import fr.tse.fise2.ui.components.EvaluationBar;


/** This class is used to display the information of a comic book chosen by the user.
 * The JLabel displays the title of the comic book and the JTextArea displays the information of the comic book.
 * The information displayed are the title, the authors, the publication date, the genre, the associated image, a mark and a synopsis.
 *
 *@author Luc SHANG
 */
public class ComicsInformation extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(ComicsInformation.class.getName());
    private static final int MAX_IMAGE_WIDTH = 500;
    private static final int MAX_IMAGE_HEIGHT = 600;
    
    private final Comic comic;
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
            FileHandler fileHandler = new FileHandler("./logs/ComicsInformation.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.config("Logger initialisé avec succès.");
        } catch (IOException e) {
            LOGGER.severe("Erreur lors de la configuration du FileHandler : " + e.getMessage());
        }
    }

    public ComicsInformation(Comic comic) {
        this.comic = Objects.requireNonNull(comic, "Comic cannot be null");
        LOGGER.info("Création panneau pour: " + comic.getName());

        // Mise en place du panel principal avec BorderLayout
        setLayout(new BorderLayout());
        this.rightPanel = createRightPanel();
        this.leftPanel = createLeftPanel(comic);

        // JsplitPnae pour séparer les 2 panels
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
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        
        addInfoComponent(panel, "<html><b><u>Title</u></b> : " + comic.getName() + "</html>", gbc, 0, 0.1);
        addInfoComponent(panel, "<html><b><u>Publication date</u></b> : " + comic.getCoverDate() + "</html>", gbc, 1, 0.1);
        
        // Ensure personCredits are populated
        String authorsString = buildDisplayList(comic.getPersonCredits());
        String charactersString = buildDisplayList(comic.getCharacterCredits());
        addInfoComponent(panel, "<html><b><u>Authors</u></b> : " + authorsString + "</html>", gbc, 2, 0.1);
        addInfoComponent(panel, "<html><b><u>Characters</u></b> : " + charactersString + "</html>", gbc, 3, 0.1);

        // Wrap description text using HTML with a specific width
        String fullDescription = comic.getDescription();
        String limitedDescription = (fullDescription != null && fullDescription.length() > 400)
            ? fullDescription.substring(0, 400) + "..."
            : fullDescription;

        String descHtml =
            "<html>" +
              "<div style='width:400px;'><b>" +
                "<span style='font-family: Arial; font-size:24px;'><u>Description</u>:</span><br>" +
                "<span style='font-family: Arial; font-size:24px;'>" + limitedDescription + "</span>" +
              "</b></div>" +
            "</html>";

        JLabel descLabel = new JLabel(descHtml);
        descLabel.setForeground(Color.WHITE);

        gbc.gridy = 4;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(descLabel, gbc);

        return panel;
        }

        private void addInfoComponent(JPanel panel, String text, GridBagConstraints gbc, int gridy, double weighty) {
        JLabel label = new JLabel(text);
        // Définir la police
        label.setFont(new java.awt.Font("Arial BLACK", java.awt.Font.BOLD, 25)); // Changed PLAIN to BOLD
        label.setForeground(Color.WHITE); // Set text color to white
        gbc.gridy = gridy;
        gbc.weighty = weighty;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Ajouter cette ligne
        gbc.anchor = GridBagConstraints.WEST;     // Ajouter cette ligne
        panel.add(label, gbc);
    }


    private JPanel createLeftPanel(Comic comic) {
        JPanel panel = new JPanel(new BorderLayout());  // Changed to BorderLayout
        panel.setBackground(new Color(11, 19, 47));  

        try {
            JLabel imageLabel = loadImage(comic.getImageUrl());
            imageLabel.setHorizontalAlignment(JLabel.CENTER);  // Center horizontally
            imageLabel.setVerticalAlignment(JLabel.CENTER);    // Center vertically
            panel.add(imageLabel, BorderLayout.CENTER);        // Add to CENTER of BorderLayout
        } catch (CharacterDisplayException e) {
            LOGGER.warning(e.getMessage());
            panel.add(createErrorLabel());
        }

        if(LoginFrame.isUserLoggedIn()==true && comic.getInLibrary()==true){

            EvaluationBar starRatingPanel=new EvaluationBar(comic);
            panel.add(starRatingPanel,BorderLayout.SOUTH);
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
            return list.get(0) + ", " + list.get(1) + ", " + list.get(2) + ", ...";
        }
        return String.join(", ", list);
    }
}