package fullstack.backend.service;

import fullstack.backend.exception.DomainValidationException;
import fullstack.backend.exception.ResourceNotFoundException;
import fullstack.backend.model.Product;
import fullstack.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getAvailableProducts() {
        return productRepository.findByIsAvailableTrue();
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        validateProduct(product);
        if (productRepository.existsById(product.getProductId())) {
            throw new DomainValidationException("Product already exists with ID: " + product.getProductId());
        }

        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    public Product updateProduct(String id, Product productDetails) {
        validateProduct(productDetails);
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        if (!existingProduct.getProductId().equals(productDetails.getProductId())) {
            throw new DomainValidationException("Product ID cannot be changed");
        }

        existingProduct.setTitle(productDetails.getTitle());
        existingProduct.setArtist(productDetails.getArtist());
        existingProduct.setLabel(productDetails.getLabel());
        existingProduct.setFormatName(productDetails.getFormatName());
        existingProduct.setFormatType(productDetails.getFormatType());
        existingProduct.setImageUrl(productDetails.getImageUrl());
        existingProduct.setReleaseYear(productDetails.getReleaseYear());
        existingProduct.setDescription(productDetails.getDescription());
        existingProduct.setPrice(productDetails.getPrice());
        existingProduct.setStockQuantity(productDetails.getStockQuantity());
        existingProduct.setAvgRating(productDetails.getAvgRating());
        existingProduct.setRatingCount(productDetails.getRatingCount());
        existingProduct.setIsAvailable(productDetails.getIsAvailable());
        existingProduct.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    public List<Product> getProductsByArtist(Long artistId) {
        return productRepository.findByArtistArtistId(artistId);
    }

    public List<Product> getProductsByFormat(String formatType) {
        return productRepository.findByFormatType(formatType);
    }

    public List<Product> searchProductsByTitle(String title) {
        return productRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Product> searchProductsByArtistName(String artistName) {
        return productRepository.findByArtistArtistNameContainingIgnoreCase(artistName);
    }

    public boolean productExists(String id) {
        return productRepository.existsById(id);
    }

    private void validateProduct(Product product) {
        if (product == null || !StringUtils.hasText(product.getProductId())) {
            throw new DomainValidationException("Product ID is required");
        }
        if (!StringUtils.hasText(product.getTitle())) {
            throw new DomainValidationException("Product title is required");
        }
        if (product.getArtist() == null) {
            throw new DomainValidationException("Artist is required");
        }
        if (product.getLabel() == null) {
            throw new DomainValidationException("Label is required");
        }
        if (!StringUtils.hasText(product.getFormatName())) {
            throw new DomainValidationException("Format name is required");
        }
        if (!StringUtils.hasText(product.getFormatType())) {
            throw new DomainValidationException("Format type is required");
        }
        if (!StringUtils.hasText(product.getImageUrl())) {
            throw new DomainValidationException("Image URL is required");
        }
        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new DomainValidationException("Product price must be greater than zero");
        }
    }
}