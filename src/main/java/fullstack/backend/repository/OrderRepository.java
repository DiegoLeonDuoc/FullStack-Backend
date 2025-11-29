package fullstack.backend.repository;

import fullstack.backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);
    List<Order> findByStatus(String status);
    List<Order> findByResponsibleUserId(Long responsibleUserId);
    List<Order> findByOrderDateBetweenAndStatus(LocalDateTime start, LocalDateTime end, String status);
    List<Order> findByStatusAndResponsibleUserId(String status, Long responsibleUserId);
    List<Order> findByOrderDateBetweenAndResponsibleUserId(LocalDateTime start, LocalDateTime end, Long responsibleUserId);
    List<Order> findByOrderDateBetweenAndStatusAndResponsibleUserId(LocalDateTime start, LocalDateTime end, String status, Long responsibleUserId);
}
