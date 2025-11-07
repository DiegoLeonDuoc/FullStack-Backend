package fullstack.backend.repository;

import fullstack.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByArtistArtistId(Long artistId);
    List<Product> findByFormatType(String formatType);
    List<Product> findByIsAvailableTrue();
    List<Product> findByTitleContainingIgnoreCase(String title);
    List<Product> findByArtistArtistNameContainingIgnoreCase(String artistName);
}