package fullstack.backend.repository;

import fullstack.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByRut(String rut);
    Boolean existsByEmail(String email);
    Boolean existsByRut(String rut);
}