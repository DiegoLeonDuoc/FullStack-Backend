package fullstack.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @Column(name = "product_id")
    @NotBlank
    private String productId;

    @Column(nullable = false)
    @NotBlank
    private String title;

    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    @NotNull
    private Artist artist;

    @ManyToOne
    @JoinColumn(name = "label_id", nullable = false)
    @NotNull
    private Label label;

    @Column(name = "format_name", nullable = false)
    @NotBlank
    private String formatName;

    @Column(name = "format_type", nullable = false)
    @NotBlank
    private String formatType;

    @Column(name = "image_url", nullable = false)
    @NotBlank
    private String imageUrl;

    @Column(name = "release_year")
    private Integer releaseYear;

    private String description;

    @Column(nullable = false)
    @NotNull
    @Positive
    private Integer price; // Price in cents

    @Column(name = "stock_quantity")
    @PositiveOrZero
    private Integer stockQuantity = 0;

    @Column(name = "avg_rating")
    private Double avgRating = 0.0;

    @Column(name = "rating_count")
    private Integer ratingCount = 0;

    @Column(name = "is_available")
    @NotNull
    private Boolean isAvailable = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}