package fr.tse.fise2.exception;

/**
 * Exception levée lorsqu'une erreur survient lors de l'affichage d'un personnage.
 * Cette exception est utilisée pour gérer les cas d'erreur spécifiques à l'affichage
 * comme le chargement d'image ou les données manquantes.
 * 
 * @author Samuel PLET
 * @see fr.tse.fise2.ui.CharactersInformation
 */
public class CharacterDisplayException extends Exception {
    public CharacterDisplayException(String message) {
        super(message);
    }
    
    public CharacterDisplayException(String message, Throwable cause) {
        super(message, cause);
    }
}