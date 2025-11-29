package fullstack.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderRequestDto {

    @NotNull
    @Positive
    private Long customerId;

    @NotBlank
    private String productId;

    @Positive
    private Long artistId;

    @Positive
    private Long labelId;

    @Positive
    private Long responsibleUserId;

    @NotNull
    @Positive
    private Integer quantity;

    @NotBlank
    private String status;
}
