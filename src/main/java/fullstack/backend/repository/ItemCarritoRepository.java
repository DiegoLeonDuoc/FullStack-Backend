package fullstack.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fullstack.backend.model.ItemCarrito;

import java.util.Optional;

public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Integer> {
    Optional<ItemCarrito> findByCarritoId(Integer carritoId);

    void deleteByCarritoId(Integer carritoId);

    void deleteBySku(String sku);
}
