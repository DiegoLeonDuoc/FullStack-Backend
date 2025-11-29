package fullstack.backend.exception;

/**
 * Excepción específica para recursos inexistentes en la capa de servicios.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
