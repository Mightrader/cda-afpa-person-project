package sparadrap.exception;

/**
 * Exception utilisée pour signaler un problème de validation de données dans les classes métier (modèle).
 */
public class ValidationException extends RuntimeException {

    /**
     * Crée une exception avec un message explicite.
     *
     * @param message détail de l'erreur
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Crée une exception avec un message et une cause technique.
     *
     * @param message détail de l'erreur
     * @param cause   cause à l'origine de l'erreur
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
