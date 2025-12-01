package fullstack.backend.repository;

import fullstack.backend.model.Sello;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SelloRepository extends JpaRepository<Sello, Long> {
    Optional<Sello> findByNombreSello(String nombreSello);

    Boolean existsByNombreSello(String nombreSello);
}
