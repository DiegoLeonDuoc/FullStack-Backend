package fullstack.backend.service;

import fullstack.backend.dto.OrderRequestDto;
import fullstack.backend.dto.OrderResponseDto;
import fullstack.backend.exception.DomainValidationException;
import fullstack.backend.exception.ResourceNotFoundException;
import fullstack.backend.model.*;
import fullstack.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private OrderBusinessService orderBusinessService;

    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll().stream().map(this::toResponseDto).toList();
    }

    public Optional<OrderResponseDto> getOrderById(Long id) {
        return orderRepository.findById(id).map(this::toResponseDto);
    }

    public OrderResponseDto createOrder(OrderRequestDto request) {
        Order order = new Order();
        order.setCustomer(findUser(request.getCustomerId()));
        Product product = orderBusinessService.validateProductAvailability(request.getProductId(), request.getQuantity());
        order.setProduct(product);
        Artist artist = resolveArtist(request, product);
        Label label = resolveLabel(request, product);
        validateArtistAndLabelConsistency(product, artist, label);
        order.setArtist(artist);
        order.setLabel(label);
        order.setResponsible(resolveUser(request.getResponsibleUserId()));
        order.setQuantity(request.getQuantity());
        order.setStatus(request.getStatus());
        order.setTotalPrice(product.getPrice() * request.getQuantity());
        order.setOrderDate(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        Order saved = orderRepository.save(order);
        return toResponseDto(saved);
    }

    public OrderResponseDto updateOrder(Long id, OrderRequestDto request) {
        Order existing = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        existing.setCustomer(findUser(request.getCustomerId()));
        Product product = orderBusinessService.validateProductAvailability(request.getProductId(), request.getQuantity());
        existing.setProduct(product);
        Artist artist = resolveArtist(request, product);
        Label label = resolveLabel(request, product);
        validateArtistAndLabelConsistency(product, artist, label);
        existing.setArtist(artist);
        existing.setLabel(label);
        existing.setResponsible(resolveUser(request.getResponsibleUserId()));
        existing.setQuantity(request.getQuantity());
        existing.setStatus(request.getStatus());
        existing.setTotalPrice(product.getPrice() * request.getQuantity());
        existing.setUpdatedAt(LocalDateTime.now());
        Order saved = orderRepository.save(existing);
        return toResponseDto(saved);
    }

    public void deleteOrder(Long id) {
        Order existing = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        orderRepository.delete(existing);
    }

    public List<OrderResponseDto> filterOrders(LocalDateTime start, LocalDateTime end, String status, Long responsibleId) {
        List<Order> orders;
        if (start != null && end != null && status != null && responsibleId != null) {
            orders = orderRepository.findByOrderDateBetweenAndStatusAndResponsibleUserId(start, end, status, responsibleId);
        } else if (start != null && end != null && status != null) {
            orders = orderRepository.findByOrderDateBetweenAndStatus(start, end, status);
        } else if (start != null && end != null && responsibleId != null) {
            orders = orderRepository.findByOrderDateBetweenAndResponsibleUserId(start, end, responsibleId);
        } else if (status != null && responsibleId != null) {
            orders = orderRepository.findByStatusAndResponsibleUserId(status, responsibleId);
        } else if (start != null && end != null) {
            orders = orderRepository.findByOrderDateBetween(start, end);
        } else if (status != null) {
            orders = orderRepository.findByStatus(status);
        } else if (responsibleId != null) {
            orders = orderRepository.findByResponsibleUserId(responsibleId);
        } else {
            orders = orderRepository.findAll();
        }
        return orders.stream().map(this::toResponseDto).toList();
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    private Product findProduct(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    private User resolveUser(Long id) {
        if (id == null) {
            return null;
        }
        return findUser(id);
    }

    private Artist resolveArtist(OrderRequestDto request, Product product) {
        if (request.getArtistId() != null) {
            return artistRepository.findById(request.getArtistId())
                    .orElseThrow(() -> new ResourceNotFoundException("Artist not found with id: " + request.getArtistId()));
        }
        return product.getArtist();
    }

    private Label resolveLabel(OrderRequestDto request, Product product) {
        if (request.getLabelId() != null) {
            return labelRepository.findById(request.getLabelId())
                    .orElseThrow(() -> new ResourceNotFoundException("Label not found with id: " + request.getLabelId()));
        }
        return product.getLabel();
    }

    private void validateArtistAndLabelConsistency(Product product, Artist artist, Label label) {
        if (artist != null && !artist.getArtistId().equals(product.getArtist().getArtistId())) {
            throw new DomainValidationException("El artista indicado no coincide con el producto");
        }
        if (label != null && !label.getLabelId().equals(product.getLabel().getLabelId())) {
            throw new DomainValidationException("El sello indicado no coincide con el producto");
        }
    }

    private OrderResponseDto toResponseDto(Order order) {
        User customer = order.getCustomer();
        User responsible = order.getResponsible();
        Product product = order.getProduct();
        Artist artist = order.getArtist();
        Label label = order.getLabel();
        return new OrderResponseDto(
                order.getOrderId(),
                order.getStatus(),
                order.getQuantity(),
                order.getTotalPrice(),
                order.getOrderDate(),
                order.getUpdatedAt(),
                customer != null ? customer.getUserId() : null,
                customer != null ? customer.getFirstName() + " " + customer.getLastName() : null,
                responsible != null ? responsible.getUserId() : null,
                responsible != null ? responsible.getFirstName() + " " + responsible.getLastName() : null,
                product != null ? product.getProductId() : null,
                product != null ? product.getTitle() : null,
                artist != null ? artist.getArtistId() : null,
                artist != null ? artist.getArtistName() : null,
                label != null ? label.getLabelId() : null,
                label != null ? label.getLabelName() : null
        );
    }
}
