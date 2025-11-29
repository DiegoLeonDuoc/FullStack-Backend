package fullstack.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private List<FieldValidationError> errors;

    public static ApiError fromBindingResult(HttpStatus status, String message, BindingResult bindingResult) {
        List<FieldValidationError> fieldErrors = new ArrayList<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            fieldErrors.add(new FieldValidationError(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        return new ApiError(status.value(), message, LocalDateTime.now(), fieldErrors);
    }

    public static ApiError simple(HttpStatus status, String message) {
        return new ApiError(status.value(), message, LocalDateTime.now(), List.of());
    }
}
