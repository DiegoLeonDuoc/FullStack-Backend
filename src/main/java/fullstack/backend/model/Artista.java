package fullstack.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "artistas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Artista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_artista", unique = true, nullable = false)
    private String nombreArtista;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;
}
