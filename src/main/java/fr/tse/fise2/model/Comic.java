package fr.tse.fise2.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

/**
 * Classe Comic
 * Cette classe permet de stocker les informations des comics.
 * Classe à améliorer ou supprimer.
 * 
 * @version 1.0
 * @author Lauriane TUTALAMO
 */
public class Comic {
    private String name;
    private  String imageUrl;
    private  List<String> aliases; // Liste des alias
    private  String apiDetailUrl; // URL des détails
    private  List<String> characterCredits; // Liste des personnages
    private  String deck; // Résumé succinct
    private  String description; // Description détaillée
    private String coverDate; // Date de publication imprimée sur la couverture
    private Boolean inFavorite; // Pour savoir si le comic est dans les favoris
    private Boolean inLibrary; // Pour savoir si le comic est dans la bibliothèque
    private Boolean inPurchase; // Pour savoir si le comic est dans la liste d'achat
    private List<String> personCredits; // Liste des crédits des personnes
    private final int id; 
    private int readingStatus; // 0-lecture prévue ,1-lecture en cours ,2-lecture finie 
    private int starsRating=0;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    // Constructeur 
    public Comic(String name, String imageUrl,List<String> aliases, String apiDetailUrl,
        List<String> characterCredits, List<String> personCredits, String deck, String description, String coverDate, int id) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.aliases = aliases;
        this.apiDetailUrl = apiDetailUrl;
        this.characterCredits = characterCredits;
        this.personCredits = personCredits;
        this.deck = deck;
        this.description = description;
        this.coverDate = coverDate;
        this.id = id;
        ComicInfoListSetter();
    }

    // Constructeur pour les comics sans description
    public Comic(String apiDetailUrl,
                   int id,boolean inLibrary,boolean inPurchase,boolean inFavorite,int readStatus,String name,String image_url,int rating) {
        this.name = name;
        this.imageUrl = image_url;
        this.aliases = null;
        this.apiDetailUrl = apiDetailUrl;
        this.characterCredits = null;
        this.deck = null;
        this.description = null;
        this.coverDate = null;
        this.id=id;
        this.inFavorite=inFavorite;
        this.inLibrary=inLibrary;
        this.inPurchase=inPurchase;
        this.readingStatus=readStatus;
        this.starsRating=rating;
    }

    // Méthode pour initialiser les listes de comics
    public void ComicInfoListSetter() {
        this.inFavorite=false;
        this.inLibrary=false;
        this.inPurchase=false;
        this.readingStatus=-1;
    }


    // Getters
    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public String getApiDetailUrl() {
        return apiDetailUrl;
    }

    public List<String> getCharacterCredits() {
        return characterCredits;
    }

    public List<String> getPersonCredits() {
        return personCredits;
    }

    public String getDeck() {
        return deck;
    }

    public String getDescription() {
        return description;
    }

    public String getCoverDate() {
        return coverDate;
    }

    public Boolean getInFavorite() {
        return inFavorite;
    }

    public Boolean getInLibrary() {
        return inLibrary;
    }

    public Boolean getInPurchase() {
        return inPurchase;
    }
    
    public int getReadingStatus() {
		return readingStatus;
	}

    public int getId(){
        return id;
    }

    public int getStarsRating(){
        return starsRating;
    }
    
    public void setReadingStatus(int readingStatus) {
		this.readingStatus = readingStatus;
	}

    
    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public void setApiDetailUrl(String apiDetailUrl) {
        this.apiDetailUrl = apiDetailUrl;
    }

    public void setCharacterCredits(List<String> characterCredits) {
        this.characterCredits = characterCredits;
    }

    public void setPersonCredits(List<String> personCredits) {
        this.personCredits = personCredits;
    }

    public void setDeck(String deck) {
        this.deck = deck;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCoverDate(String coverDate) {
        this.coverDate = coverDate;
    }

    public void setInFavorite(Boolean inFavorite) {
        Boolean oldInFavorite = this.inFavorite;
        this.inFavorite = inFavorite;
        pcs.firePropertyChange("inFavorite", oldInFavorite, inFavorite);
    }

    public void setInLibrary(Boolean inLibrary) {
        Boolean oldInLibrary = this.inLibrary;
        this.inLibrary = inLibrary;
        pcs.firePropertyChange("inLibrary", oldInLibrary, inLibrary);
    }

    public void setInPurchase(Boolean inPurchase) {
        Boolean oldInPurchase = this.inPurchase;
        this.inPurchase = inPurchase;
        pcs.firePropertyChange("inPurchase", oldInPurchase, inPurchase);
    }

    public void setStarsRating(int rating){
        this.starsRating=rating;
    }

    public void setComic(Comic comic){
        this.name = comic.getName();
        this.imageUrl = comic.getImageUrl();
        this.aliases = comic.getAliases();
        this.apiDetailUrl = comic.getApiDetailUrl();
        this.characterCredits = comic.getCharacterCredits();
        this.personCredits = comic.getPersonCredits();
        this.deck = comic.getDeck();
        this.description = comic.getDescription();
        this.coverDate = comic.getCoverDate();
    }

    // Méthode pour afficher les informations du comic
    @Override
    public String toString() {
        return "ComicInfo{" +
                "name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", aliases=" + String.join(", ", aliases) +
                ", apiDetailUrl='" + apiDetailUrl + '\'' +
                ", characterCredits=" + String.join(", ", characterCredits) +
                ", personCredits=" + String.join(", ", personCredits) +
                ", deck='" + deck + '\'' +
                ", description='" + description + '\'' +
                ", coverDate='" + coverDate + '\'' +
                ", inFavorite=" + inFavorite +
               ", inLibrary=" + inLibrary +
               ", inPurchase=" + inPurchase + '\'' +
               ", id=" + id +
                '}';
    }

    // Add property change listeners
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}