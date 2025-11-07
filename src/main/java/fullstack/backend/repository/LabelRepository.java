package fullstack.backend.repository;

import fullstack.backend.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {
    Optional<Label> findByLabelName(String labelName);
    Boolean existsByLabelName(String labelName);
}
