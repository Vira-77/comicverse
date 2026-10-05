package fr.tse.fise2.model;

import fr.tse.fise2.api.ApiClient;
import fr.tse.fise2.api.CharacterExtractor;
import fr.tse.fise2.exception.ApiException;

import java.util.List;

import org.jsoup.Jsoup;

/**
 * Classe Character
 * Cette classe permet de stocker les informations d'un personnage.
 * 
 * @version 1.0
 * @author Lauriane TUTALAMO
 */
public class ComicsCharacter {
	private  String heroName;
	private  String realName;
	private  String aliases;
	private  String photoUrl;
	private  String birthDate;
	private  String shortDescription;
	private  String longDescription;
	private  List<String> issueCredits;
	private  List<String> volumeCredits;
	private  String publisherName;
	private  String publisherImage;
	private  String origin;
	private  String apiDetailUrl;
	private final int id;

	// Constructeur 
	public ComicsCharacter(String heroName, String realName, String aliases, String photoUrl, String birthDate,
	String shortDescription, String longDescription, List<String> issueCredit, List<String> volumeCredit, String publisherName, String publisherImage,
	String origin,String apiDetailUrl, int id) {
		this.heroName = heroName;
		this.realName = realName;
		this.aliases = aliases;
		this.photoUrl = photoUrl;
		this.birthDate = birthDate;
		this.shortDescription = shortDescription;
		this.longDescription = longDescription;
		this.issueCredits = issueCredit;
		this.volumeCredits = volumeCredit;
		this.publisherName = publisherName;
		this.publisherImage = publisherImage; 
		this.origin = origin;
		this.apiDetailUrl = apiDetailUrl;
		this.id = id;
	} 
	
	// Méthode pour supprimer les balises HTML
	private String removeHtmlTags(String html) {
		return Jsoup.parse(html).text();
	}

	// Méthode pour compléter un Character à partir de son id
	public void completeCharacter(ApiClient apiClient) throws ApiException {
		if (this.id <= 0) {
			throw new IllegalStateException("Impossible de compléter le personnage sans un ID valide.");
		}

		// Appel de getDetailsById pour obtenir les informations détaillées
		ComicsCharacter detailedCharacter = CharacterExtractor.getCharacterById(apiClient, this.id);

		// Copier les détails manquants depuis detailedCharacter
		this.realName = detailedCharacter.getRealName();
		this.aliases = detailedCharacter.getAliases();
		this.birthDate = detailedCharacter.getBirthDate();
		this.longDescription = detailedCharacter.getLongDescription();
		this.issueCredits = detailedCharacter.getIssueCredits();
		this.volumeCredits = detailedCharacter.getVolumeCredits();
		this.publisherName = detailedCharacter.getPublisherName();
		this.publisherImage = detailedCharacter.getPublisherImage();
		this.origin = detailedCharacter.getOrigin();
		this.apiDetailUrl = detailedCharacter.getApiDetailUrl();
	}

	// Getters
	public String getHeroName() {
		return heroName;
	}

	public String getRealName() {
		return realName;
	}

	public String getAliases() {
		return aliases;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public List<String> getIssueCredits() {
		return issueCredits;
	}

	public List<String> getVolumeCredits() {
		return volumeCredits;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public String getPublisherImage() {
		return publisherImage;
	}

	 // Méthode pour obtenir la longue description sans HTML
	 public String getLongDescription() {
		return removeHtmlTags(this.longDescription);
	}

	public String getOrigin(){
		return origin;
	}

	public String getApiDetailUrl(){
		return apiDetailUrl;
	}

	public int getId(){
		return id;
	}

	// Setters
	public void setHeroName(String heroName) {
		this.heroName = heroName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public void setAliases(String aliases) {
		this.aliases = aliases;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public void setIssueCredits(List<String> issueCredits) {
		this.issueCredits = issueCredits;
	}

	public void setVolumeCredits(List<String> volumeCredits) {
		this.volumeCredits = volumeCredits;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public void setPublisherImage(String publisherImage) {
		this.publisherImage = publisherImage;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public void setApiDetailUrl(String apiDetailUrl) {
		this.apiDetailUrl = apiDetailUrl;
	}

	// Méthode toString
	@Override
	public String toString() {
		return "Character Information:\n" +
				"----------------------\n" +
				"Hero Name: " + (heroName != null ? heroName : "Unknown") + "\n" +
				"Real Name: " + (realName != null ? realName : "Unknown") + "\n" +
				"Aliases: " + (aliases != null ? aliases : "No aliases") + "\n" +
				"Photo URL: " + (photoUrl != null ? photoUrl : "No photo") + "\n" +
				"Birth Date: " + (birthDate != null ? birthDate : "Unknown") + "\n" +
				"Short Description: " + (shortDescription != null ? shortDescription : "No description") + "\n" +
				"Long Description: " + (longDescription != null ? longDescription : "No description") + "\n" +
				"Issue Credits: " + (issueCredits != null ? String.join(", ", issueCredits) : "None") + "\n" +
				"Volume Credits: " + (volumeCredits != null ? String.join(", ", volumeCredits) : "None") + "\n" +
				"Publisher: " + (publisherName != null ? publisherName : "Unknown") + "\n" +
				"Publisher Image URL: " + (publisherImage != null ? publisherImage : "No image") + "\n" +
				"Origin: " + (origin != null ? origin : "Unknown") + "\n" +
				"API Detail URL: " + (apiDetailUrl != null ? apiDetailUrl : "Unknown") + "\n" +
				"ID: " + id + "\n";
	}
}