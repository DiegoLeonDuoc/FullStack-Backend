package fullstack.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    @Id
    @Column(name = "sku", unique = true, nullable = false, length = 50)
    private String sku;

    @Column(nullable = false)
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "id_artista", nullable = false)
    private Artista artista;

    @ManyToOne
    @JoinColumn(name = "id_sello", nullable = false)
    private Sello sello;

    @Column(name = "nombre_formato", nullable = false)
    private String nombreFormato;

    @Column(name = "tipo_formato", nullable = false)
    private String tipoFormato;

    @Column(name = "url_imagen", nullable = false)
    private String urlImagen;

    @Column(name = "anio_lanzamiento")
    private Integer anioLanzamiento;

    private String descripcion;

    @Column(nullable = false)
    private Integer precio; // Price in cents

    @Column(name = "cantidad_stock")
    private Integer cantidadStock = 0;

    @Column(name = "calificacion_promedio")
    private Double calificacionPromedio = 0.0;

    @Column(name = "conteo_calificaciones")
    private Integer conteoCalificaciones = 0;

    @Column(name = "esta_disponible")
    private Boolean estaDisponible = true;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}
