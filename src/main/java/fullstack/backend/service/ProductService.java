package fullstack.backend.service;

import fullstack.backend.model.Product;
import fullstack.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        // Validate unique constraint
        if (productRepository.existsById(product.getProductId())) {
            throw new RuntimeException("Product already exists with ID: " + product.getProductId());
        }

        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    public Product updateProduct(String id, Product productDetails) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            Product existingProduct = product.get();

            // Check if ID is being changed (not allowed as it's the primary key)
            if (!existingProduct.getProductId().equals(productDetails.getProductId())) {
                throw new RuntimeException("Product ID cannot be changed");
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
        throw new RuntimeException("Product not found with id: " + id);
    }

    public void deleteProduct(String id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.delete(product.get());
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
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
}