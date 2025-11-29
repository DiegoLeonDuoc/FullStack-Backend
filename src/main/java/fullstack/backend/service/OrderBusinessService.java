package fullstack.backend.service;

import fullstack.backend.exception.DomainValidationException;
import fullstack.backend.exception.ResourceNotFoundException;
import fullstack.backend.model.Product;
import fullstack.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class OrderBusinessService {

    private final ProductRepository productRepository;

    public OrderBusinessService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product validateProductAvailability(String productId, int quantity) {
        if (!StringUtils.hasText(productId)) {
            throw new DomainValidationException("El identificador de producto no puede estar vacío");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productId));

        if (Boolean.FALSE.equals(product.getIsAvailable())) {
            throw new DomainValidationException("El producto " + productId + " no se encuentra disponible");
        }

        if (product.getStockQuantity() != null && product.getStockQuantity() < quantity) {
            throw new DomainValidationException("Stock insuficiente para el producto " + productId);
        }

        return product;
    }
}
