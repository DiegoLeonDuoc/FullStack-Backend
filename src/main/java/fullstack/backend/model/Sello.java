package fullstack.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "sellos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sello {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_sello", unique = true, nullable = false)
    private String nombreSello;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;
}
