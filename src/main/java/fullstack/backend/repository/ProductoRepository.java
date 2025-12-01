package fullstack.backend.repository;

import fullstack.backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, String> {
    List<Producto> findByArtistaId(Long artistaId);

    List<Producto> findByTipoFormato(String tipoFormato);

    List<Producto> findByEstaDisponibleTrue();

    List<Producto> findByTituloContainingIgnoreCase(String titulo);

    List<Producto> findByArtistaNombreArtistaContainingIgnoreCase(String nombreArtista);
}
