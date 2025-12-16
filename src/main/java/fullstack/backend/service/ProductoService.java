package fullstack.backend.service;

import fullstack.backend.model.Producto;
import fullstack.backend.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    public List<Producto> getAvailableProductos() {
        return productoRepository.findByEstaDisponibleTrue();
    }

    public Optional<Producto> getProductoById(String id) {
        return productoRepository.findById(id);
    }

    public Producto createProducto(Producto producto) {
        // Validate unique constraint
        if (productoRepository.existsById(producto.getSku())) {
            throw new RuntimeException("Producto ya existe con SKU: " + producto.getSku());
        }

        producto.setCreadoEn(LocalDateTime.now());
        producto.setActualizadoEn(LocalDateTime.now());
        return productoRepository.save(producto);
    }

    public Producto updateProducto(String id, Producto productoDetails) {
        Optional<Producto> producto = productoRepository.findById(id);
        if (producto.isPresent()) {
            Producto existingProducto = producto.get();

            // Check if ID is being changed (not allowed as it's the primary key)
            if (!existingProducto.getSku().equals(productoDetails.getSku())) {
                throw new RuntimeException("El SKU del producto no puede ser cambiado");
            }

            existingProducto.setTitulo(productoDetails.getTitulo());
            existingProducto.setArtista(productoDetails.getArtista());
            existingProducto.setSello(productoDetails.getSello());
            existingProducto.setNombreFormato(productoDetails.getNombreFormato());
            existingProducto.setTipoFormato(productoDetails.getTipoFormato());
            existingProducto.setUrlImagen(productoDetails.getUrlImagen());
            existingProducto.setAnioLanzamiento(productoDetails.getAnioLanzamiento());
            existingProducto.setDescripcion(productoDetails.getDescripcion());
            existingProducto.setPrecio(productoDetails.getPrecio());
            existingProducto.setCantidadStock(productoDetails.getCantidadStock());
            existingProducto.setCalificacionPromedio(productoDetails.getCalificacionPromedio());
            existingProducto.setConteoCalificaciones(productoDetails.getConteoCalificaciones());
            existingProducto.setEstaDisponible(productoDetails.getEstaDisponible());
            existingProducto.setActualizadoEn(LocalDateTime.now());

            return productoRepository.save(existingProducto);
        }
        throw new RuntimeException("Producto no encontrado con id: " + id);
    }

    @Autowired
    private fullstack.backend.repository.ItemCarritoRepository itemCarritoRepository;

    @org.springframework.transaction.annotation.Transactional
    public void deleteProducto(String id) {
        Optional<Producto> producto = productoRepository.findById(id);
        if (producto.isPresent()) {
            itemCarritoRepository.deleteBySku(id);
            productoRepository.delete(producto.get());
        } else {
            throw new RuntimeException("Producto no encontrado con id: " + id);
        }
    }

    public List<Producto> getProductosByArtista(Long artistaId) {
        return productoRepository.findByArtistaId(artistaId);
    }

    public List<Producto> getProductosByFormato(String tipoFormato) {
        return productoRepository.findByTipoFormato(tipoFormato);
    }

    public List<Producto> searchProductosByTitulo(String titulo) {
        return productoRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Producto> searchProductosByNombreArtista(String nombreArtista) {
        return productoRepository.findByArtistaNombreArtistaContainingIgnoreCase(nombreArtista);
    }

    public boolean productoExists(String id) {
        return productoRepository.existsById(id);
    }
}
