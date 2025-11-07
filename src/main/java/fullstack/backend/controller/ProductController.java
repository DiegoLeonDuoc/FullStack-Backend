package fullstack.backend.controller;

import fullstack.backend.assembler.ProductModelAssembler;
import fullstack.backend.model.Product;
import fullstack.backend.service.ProductService;
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
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Controlador Producto", description = "Servicios de gestión de productos")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductModelAssembler assembler;

    // C
    @PostMapping
    @Operation(summary = "Agregar Producto", description = "Permite registrar un producto en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado",
                    content = @Content(mediaType = "application/json",
                            schema =@Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "JSON con mal formato o producto duplicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<Product>> createProduct(@RequestBody @Valid Product product) {
        try {
            Product createdProduct = productService.createProduct(product);
            return new ResponseEntity<>(assembler.toModel(createdProduct), HttpStatus.CREATED);
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
    public ResponseEntity<CollectionModel<EntityModel<Product>>> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            if (products.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(assembler.toCollectionModel(products), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/available")
    @Operation(summary = "Obtener productos disponibles", description = "Obtiene la lista de productos disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna lista de productos disponibles"),
            @ApiResponse(responseCode = "404", description = "No se encuentran productos disponibles", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CollectionModel<EntityModel<Product>>> getAvailableProducts() {
        try {
            List<Product> products = productService.getAvailableProducts();
            if (products.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(assembler.toCollectionModel(products), HttpStatus.OK);
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
    public ResponseEntity<EntityModel<Product>> getProductById(@PathVariable String id) {
        try {
            Optional<Product> productOptional = productService.getProductById(id);
            if (productOptional.isPresent()) {
                return new ResponseEntity<>(assembler.toModel(productOptional.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // U
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Permite actualizar los datos de un producto según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto modificado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "400", description = "JSON con mal formato"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El ID del producto", example = "abbey-road-vinilo")
    public ResponseEntity<EntityModel<Product>> updateProduct(@PathVariable String id, @RequestBody @Valid Product product) {
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            return new ResponseEntity<>(assembler.toModel(updatedProduct), HttpStatus.OK);
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
    @Operation(summary = "Eliminar producto", description = "Elimina un producto específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna el producto eliminado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El ID del producto", example = "abbey-road-vinilo")
    public ResponseEntity<EntityModel<Product>> deleteProduct(@PathVariable String id) {
        try {
            Optional<Product> productOptional = productService.getProductById(id);
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                productService.deleteProduct(id);
                return new ResponseEntity<>(assembler.toModel(product), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/artist/{artistId}")
    @Operation(summary = "Buscar productos por artista", description = "Obtiene productos según el ID del artista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna lista de productos del artista"),
            @ApiResponse(responseCode = "404", description = "No se encuentran productos para el artista", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El ID del artista", example = "1")
    public ResponseEntity<CollectionModel<EntityModel<Product>>> getProductsByArtist(@PathVariable Long artistId) {
        try {
            List<Product> products = productService.getProductsByArtist(artistId);
            if (products.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(assembler.toCollectionModel(products), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/format/{formatType}")
    @Operation(summary = "Buscar productos por formato", description = "Obtiene productos según el tipo de formato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna lista de productos del formato"),
            @ApiResponse(responseCode = "404", description = "No se encuentran productos para el formato", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El tipo de formato", example = "VINYL")
    public ResponseEntity<CollectionModel<EntityModel<Product>>> getProductsByFormat(@PathVariable String formatType) {
        try {
            List<Product> products = productService.getProductsByFormat(formatType);
            if (products.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(assembler.toCollectionModel(products), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}