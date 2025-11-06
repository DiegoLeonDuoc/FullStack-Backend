package fullstack.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    // Getters and Setters
    @Id
    @Column(name = "product_id")
    private String productId;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @ManyToOne
    @JoinColumn(name = "label_id", nullable = false)
    private Label label;

    @Column(name = "format_name", nullable = false)
    private String formatName;

    @Column(name = "format_type", nullable = false)
    private String formatType;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "release_year")
    private Integer releaseYear;

    private String description;

    @Column(nullable = false)
    private Integer price; // Price in cents

    @Column(name = "stock_quantity")
    private Integer stockQuantity = 0;

    @Column(name = "avg_rating")
    private Double avgRating = 0.0;

    @Column(name = "rating_count")
    private Integer ratingCount = 0;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}