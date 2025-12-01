package fullstack.backend.service;

import fullstack.backend.model.ItemCarrito;
import fullstack.backend.model.Usuario;
import fullstack.backend.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fullstack.backend.model.Carrito;
import fullstack.backend.repository.CarritoRepository;
import fullstack.backend.repository.ItemCarritoRepository;

import java.util.Optional;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ItemCarritoRepository itemCarritoRepository;

    public Carrito crearUObtenerCarrito(Integer rutUsuario) {
        Optional<Usuario> usuario = usuarioRepository.findByRut(rutUsuario);
        if (usuario.isPresent()) {
            return carritoRepository.findByUsuario(usuario.get())
                    .orElseGet(() -> {
                        Carrito carrito = new Carrito();
                        carrito.setUsuario(usuario.get());
                        return carritoRepository.save(carrito);
                    }
                );
        }
        return null;
    }

    @Transactional
    public Carrito obtenerCarritoPorRut(Integer rutUsuario) {
        Optional<Usuario> usuario = usuarioRepository.findByRut(rutUsuario);
        if (usuario.isPresent()) {
            return carritoRepository.findByUsuario(usuario.get())
                    .orElse(crearUObtenerCarrito(rutUsuario));
        }
        return null;
    }

    public void borrarCarrito(Integer id) {
        carritoRepository.deleteById(id);
    }

    @Transactional
    public Carrito agregarItemCarrito(Integer rutUsuario, Integer idProducto, Integer cantidad) {
        Carrito carrito = crearUObtenerCarrito(rutUsuario);

        Optional<ItemCarrito> itemsExistente = carrito.getItemsCarrito().stream()
                .filter(i -> i.getIdProducto().equals(idProducto))
                .findFirst();

        if (itemsExistente.isPresent()) {
            itemsExistente.get().setCantidad(itemsExistente.get().getCantidad() + cantidad);
        } else {
            ItemCarrito itemCarrito = new ItemCarrito();
            itemCarrito.setIdProducto(idProducto);
            itemCarrito.setCantidad(cantidad);
            itemCarrito.setCarrito(carrito);
            carrito.getItemsCarrito().add(itemCarrito);
        }

        return carritoRepository.save(carrito);
    }

    @Transactional
    public Carrito actualizarCantidadItem(Integer rutUsuario, Integer itemId, Integer nuevaCantidad) {
        Carrito carrito = crearUObtenerCarrito(rutUsuario);

        ItemCarrito itemsExistente = carrito.getItemsCarrito().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No existe el item con el id: " + itemId));

        itemsExistente.setCantidad(nuevaCantidad);

        return carritoRepository.save(carrito);
    }

    @Transactional
    public Carrito borrarItemCarrito(Integer rutUsuario, Integer idProducto) {
        Carrito  carrito = obtenerCarritoPorRut(rutUsuario);

        carrito.getItemsCarrito().removeIf(i -> i.getId().equals(idProducto));

        return carritoRepository.save(carrito);
    }

    @Transactional
    public Carrito vaciarCarrito(Integer rutUsuario) {
        Carrito carrito = obtenerCarritoPorRut(rutUsuario);
        carrito.getItemsCarrito().clear();
        return carritoRepository.save(carrito);
    }

}
