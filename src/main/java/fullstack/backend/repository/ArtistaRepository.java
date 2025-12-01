package fullstack.backend.repository;

import fullstack.backend.model.Artista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistaRepository extends JpaRepository<Artista, Long> {
    Optional<Artista> findByNombreArtista(String nombreArtista);

    Boolean existsByNombreArtista(String nombreArtista);
}
