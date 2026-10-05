package fr.tse.fise2.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.net.URL;

import fr.tse.fise2.model.Comic;
import fr.tse.fise2.model.ComicManager;
import fr.tse.fise2.model.Library;

/**
 * Classe ComicButton
 * Cette classe permet de créer des boutons pour les comics.
 * Elle peut servir de modèle pour tous les boutons à afficher.
 * 
 * @autor Lauriane TUTALAMO & Samuel PLET
 */
public class ComicButton extends JButton implements PropertyChangeListener  {

    private final Comic comic;
    private final ButtonType type;
    private ImageIcon imageIconNormal;
    private ImageIcon imageIconClicked;
   
    // Enumération des types de boutons
    public enum ButtonType {
        FAVORITE("Ajouter aux favoris","favorite_normal.png","favorite_clicked.png"), //Ajout pour chaque type de bouton , une icône normale et une qui apparait quand on clique dessus
        LIBRARY("Ajouter à la bibliothèque","plus.png","moins.png"),
        WISHLIST("Ajouter à la liste d'achats","addCart.png","removeCart.png");

        private final String label;
        private final String normalIcon;
        private final String clickedIcon;

        ButtonType(String label, String normalIcon, String clickedIcon) {
            this.label = label;
            this.normalIcon = normalIcon;
            this.clickedIcon = clickedIcon;
        }

        public String getLabel() {
            return label;
        }

        public String getNormalIcon() {
            return normalIcon;
        }
    
        public String getClickedIcon() {
            return clickedIcon;
        }
    }

    public ButtonType getType() {
        return this.type;
    }

    public ComicButton(Comic comic, ButtonType type) {
        super(type.getLabel());
        this.comic = comic;
        this.type = type;

        // Add listener to comic for property changes
        comic.addPropertyChangeListener(this);

        this.setText(null);
        this.setMargin(new Insets(0, 0, 0, 0)); 

        // Charger les icônes dynamiques
        this.imageIconNormal = loadIcon(type.getNormalIcon());
        this.imageIconClicked = loadIcon(type.getClickedIcon());

        // Appliquer l'icône initiale
        if (type==ButtonType.FAVORITE){
            
            if (comic.getInFavorite() == null || comic.getInFavorite()==false) {
                setIcon(imageIconNormal);  // Icône normale si pas favori
            } else {
                setIcon(imageIconClicked); // Icône cliquée si favori
            }
        }

        if (type==ButtonType.LIBRARY){

            if (comic.getInLibrary() == null || comic.getInLibrary()==false) {
                setIcon(imageIconNormal);  // Icône normale si pas dans la bibliothèque
            } else {
                setIcon(imageIconClicked); // Icône cliquée si dans la bibliothèque
            }
        }

        if (type==ButtonType.WISHLIST){
            
            if (comic.getInPurchase() == null || comic.getInPurchase()==false) {
                setIcon(imageIconNormal);  // Icône normale si pas favori
            } else {
                setIcon(imageIconClicked); // Icône cliquée si favori
            }
        }
        addActionListener(createActionListener());
    }

    protected ActionListener createActionListener() {
        return e -> {
            switch (type) {
                case FAVORITE:
                    handleFavorite();
                    break;
                case LIBRARY:
                    handleLibrary();
                    break;
                case WISHLIST:
                    handleWishlist();
                    break;
            }
        };
    }

    private ImageIcon loadIcon(String iconName) {
        try {
            URL iconUrl = getClass().getClassLoader().getResource("images/" + iconName);
            if (iconUrl != null) {
                ImageIcon icon = new ImageIcon(iconUrl);
                Image scaledImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Icône par défaut si l'image est introuvable
    }
    
    public void toggleIcon() {
        if (getIcon() == imageIconNormal) {
            setIcon(imageIconClicked);
        } else {
            setIcon(imageIconNormal);
        }
    }

    private void handleFavorite() {
        // Vérifier si le comic est déjà dans les favoris
        if (ComicManager.isComicInFavorite(comic)) {
            // Si c'est déjà un favori, on le retire
            toggleIcon();
            ComicManager.removeComicFromFavorites(comic); 
            
            System.out.println("Le comic " + comic.getName() + " a été retiré des favoris.");
        } else {
            // Sinon, on l'ajoute aux favoris
            toggleIcon();
            ComicManager.addComicToFavorites(comic);
            System.out.println("Le comic " + comic.getName() + " a été ajouté aux favoris.");
        }
    }

    private void handleLibrary() {
        // Simuler l'ajout du comic à la bibliothèque
    	if (ComicManager.isComicInLibrary(comic)) {            
    		toggleIcon();
    		System.out.println("Le comic " + comic.getName() + " a été retiré de la bibliothèque.");
    		ComicManager.removeComicFromLibrary(comic);
    	} else {
    		Library.readingStatus(comic);
    		if (comic.getReadingStatus()==0 ||comic.getReadingStatus()==1 ||comic.getReadingStatus()==2) {
    			toggleIcon();
                //appeler méthode pour ajouter un comic dans la database
    			System.out.println("Le comic " + comic.getName() + " a été ajouté à la bibliothèque.");
    			ComicManager.addComicToLibrary(comic);
    		}
    	}
    }

    private void handleWishlist() {
        // Simuler l'ajout du comic à la liste d'achats
        if (ComicManager.isComicInWishList(comic)) {
    		toggleIcon();
    		System.out.println("Le comic " + comic.getName() + " a été retiré de la wishList.");
    	    ComicManager.removeComicFromWishList(comic);  
    	} else {
            toggleIcon();
            ComicManager.addComicToWishList(comic);
            System.out.println("Le comic " + comic.getName() + " a été ajouté à la wishLIst.");
    	}
    }

    public void updateIcon() {
        switch (type) {
            case FAVORITE:
                if (comic.getInFavorite() == null || !comic.getInFavorite()) {
                    setIcon(imageIconNormal);
                } else {
                    setIcon(imageIconClicked);
                }
                break;
            case LIBRARY:
                if (comic.getInLibrary() == null || !comic.getInLibrary()) {
                    setIcon(imageIconNormal);
                } else {
                    setIcon(imageIconClicked);
                }
                break;
            case WISHLIST:
                if (comic.getInPurchase() == null || !comic.getInPurchase()) {
                    setIcon(imageIconNormal);
                } else {
                    setIcon(imageIconClicked);
                }
                break;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // If the property changes for the corresponding button type, update the button's text
        if ("inLibrary".equals(evt.getPropertyName())) {
            updateIcon(); // Update the button text based on the new state
        }
        if ("inPurchase".equals(evt.getPropertyName())) {
            updateIcon(); // Update the button text based on the new state
        }

        if ("inFavorite".equals(evt.getPropertyName())) {
            updateIcon(); // Update the button text based on the new state
        }
    }
}