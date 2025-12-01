package fullstack.backend.controller;

import fullstack.backend.assembler.ProductoModelAssembler;
import fullstack.backend.model.Producto;
import fullstack.backend.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Controlador Producto", description = "Servicios de gestión de productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoModelAssembler assembler;

    // C
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Agregar Producto", description = "Permite registrar un producto en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Producto.class))),
            @ApiResponse(responseCode = "400", description = "JSON con mal formato o producto duplicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<Producto>> createProducto(@RequestBody @Valid Producto producto) {
        try {
            Producto createdProducto = productoService.createProducto(producto);
            return new ResponseEntity<>(assembler.toModel(createdProducto), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // R
    @GetMapping
    @Operation(summary = "Obtener productos", description = "Obtiene la lista de productos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna lista completa de productos"),
            @ApiResponse(responseCode = "404", description = "No se encuentran productos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> getAllProductos() {
        try {
            List<Producto> productos = productoService.getAllProductos();
            if (productos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(assembler.toCollectionModel(productos), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Obtener productos disponibles", description = "Obtiene la lista de productos disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna lista de productos disponibles"),
            @ApiResponse(responseCode = "404", description = "No se encuentran productos disponibles", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> getAvailableProductos() {
        try {
            List<Producto> productos = productoService.getAvailableProductos();
            if (productos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(assembler.toCollectionModel(productos), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar producto por ID", description = "Obtiene un producto según el ID registrado en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna Producto"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El ID del producto", example = "abbey-road-vinilo")
    public ResponseEntity<EntityModel<Producto>> getProductoById(@PathVariable String id) {
        try {
            Optional<Producto> productoOptional = productoService.getProductoById(id);
            if (productoOptional.isPresent()) {
                return new ResponseEntity<>(assembler.toModel(productoOptional.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // U
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar producto", description = "Permite actualizar los datos de un producto según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto modificado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Producto.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "400", description = "JSON con mal formato"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El ID del producto", example = "abbey-road-vinilo")
    public ResponseEntity<EntityModel<Producto>> updateProducto(@PathVariable String id,
            @RequestBody @Valid Producto producto) {
        try {
            Producto updatedProducto = productoService.updateProducto(id, producto);
            return new ResponseEntity<>(assembler.toModel(updatedProducto), HttpStatus.OK);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // D
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna el producto eliminado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El ID del producto", example = "abbey-road-vinilo")
    public ResponseEntity<EntityModel<Producto>> deleteProducto(@PathVariable String id) {
        try {
            Optional<Producto> productoOptional = productoService.getProductoById(id);
            if (productoOptional.isPresent()) {
                Producto producto = productoOptional.get();
                productoService.deleteProducto(id);
                return new ResponseEntity<>(assembler.toModel(producto), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/artista/{artistaId}")
    @Operation(summary = "Buscar productos por artista", description = "Obtiene productos según el ID del artista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna lista de productos del artista"),
            @ApiResponse(responseCode = "404", description = "No se encuentran productos para el artista", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El ID del artista", example = "1")
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> getProductosByArtista(@PathVariable Long artistaId) {
        try {
            List<Producto> productos = productoService.getProductosByArtista(artistaId);
            if (productos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(assembler.toCollectionModel(productos), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/formato/{tipoFormato}")
    @Operation(summary = "Buscar productos por formato", description = "Obtiene productos según el tipo de formato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna lista de productos del formato"),
            @ApiResponse(responseCode = "404", description = "No se encuentran productos para el formato", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El tipo de formato", example = "VINYL")
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> getProductosByFormato(
            @PathVariable String tipoFormato) {
        try {
            List<Producto> productos = productoService.getProductosByFormato(tipoFormato);
            if (productos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(assembler.toCollectionModel(productos), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
