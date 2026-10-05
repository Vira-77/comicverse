package fr.tse.fise2.exception;

/**
 * Exception levée lorsqu'une erreur survient lors de l'utilisation de l'API
 * 
 * @autor Samuel PLET
 */
public class ApiException extends Exception {

    // Constructeurs
    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
