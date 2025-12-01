package fullstack.backend.repository;

import fullstack.backend.model.Carrito;
import fullstack.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Integer> {
    Optional<Carrito> findByUsuario(Usuario usuario);
}
