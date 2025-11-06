package fullstack.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "artists")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long artistId;

    @Column(name = "artist_name", unique = true, nullable = false)
    private String artistName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}