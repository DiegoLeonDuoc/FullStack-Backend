package fullstack.backend.exception;

/**
 * Excepción de negocio utilizada para representar violaciones de reglas de dominio
 * o inconsistencias detectadas en los servicios de aplicación.
 */
public class DomainValidationException extends RuntimeException {

    public DomainValidationException(String message) {
        super(message);
    }
}
