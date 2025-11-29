package fullstack.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Long orderId;
    private String status;
    private Integer quantity;
    private Integer totalPrice;
    private LocalDateTime orderDate;
    private LocalDateTime updatedAt;
    private Long customerId;
    private String customerName;
    private Long responsibleUserId;
    private String responsibleName;
    private String productId;
    private String productTitle;
    private Long artistId;
    private String artistName;
    private Long labelId;
    private String labelName;
}
