package fullstack.backend.controller;

import fullstack.backend.assembler.CarritoModelAssembler;
import fullstack.backend.model.Carrito;
import fullstack.backend.service.CarritoService;
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
@RequestMapping("/api/v1/carritos")
@Tag(name = "Controlador Carrito", description = "Servicios de gestión de carritos")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private CarritoModelAssembler assembler;

    // C
    @PostMapping("/{rutUsuario}")
    @Operation(summary = "Agregar Carrito", description = "Permite registrar un carrito en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Carrito creado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Carrito.class))),
            @ApiResponse(responseCode = "400", description = "JSON con mal formato o carrito duplicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<Carrito>> crearCarrito(@PathVariable Integer rutUsuario) {
        try {
            Carrito carritoCreado = carritoService.crearUObtenerCarrito(rutUsuario);
            return new ResponseEntity<>(assembler.toModel(carritoCreado), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // R
    // @GetMapping
    // @Operation(summary = "Obtener carritos", description = "Obtiene la lista de
    // carritos registrados")
    // @ApiResponses(value = {
    // @ApiResponse(responseCode = "200", description = "Retorna lista completa de
    // carritos"),
    // @ApiResponse(responseCode = "404", description = "No se encuentran carritos",
    // content = @Content),
    // @ApiResponse(responseCode = "500", description = "Error interno del
    // servidor")
    // })
    // public ResponseEntity<CollectionModel<EntityModel<Carrito>>>
    // obtenerCarritos() {
    // try {
    // // List<Carrito> carritos = carritoService.getAllCarritos();
    // // if (carritos.isEmpty()) {
    // // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    // // } else {
    // // return new ResponseEntity<>(assembler.toCollectionModel(carritos),
    // HttpStatus.OK);
    // // }
    // return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    // } catch (Exception e) {
    // return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    // }

    // @GetMapping("/{id}")
    // @Operation(summary = "Buscar carrito por ID", description = "Obtiene un
    // carrito según el ID registrado en el sistema")
    // @ApiResponses(value = {
    // @ApiResponse(responseCode = "200", description = "Retorna Carrito"),
    // @ApiResponse(responseCode = "404", description = "Carrito no encontrado",
    // content = @Content),
    // @ApiResponse(responseCode = "500", description = "Error interno del
    // servidor")
    // })
    // @Parameter(description = "El ID del carrito", example = "abbey-road-vinilo")
    // public ResponseEntity<EntityModel<Carrito>> getCarritoById(@PathVariable
    // Integer id) {
    // try {
    // // Optional<Carrito> carritoOptional = carritoService.getCarritoById(id);
    // // if (carritoOptional.isPresent()) {
    // // return new ResponseEntity<>(assembler.toModel(carritoOptional.get()),
    // HttpStatus.OK);
    // // } else {
    // // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    // // }
    // return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    // } catch (Exception e) {
    // return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    // }

    @GetMapping("/{rut}")
    @Operation(summary = "Buscar carrito por RUT", description = "Obtiene un carrito según el RUT de un usuario registrado en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna Carrito"),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El RUT del usuario al que pertenece el carrito del carrito", example = "12345678")
    public ResponseEntity<EntityModel<Carrito>> obtenerCarritoPorRut(@PathVariable Integer rut) {
        try {
            Carrito carrito = carritoService.obtenerCarritoPorRut(rut);
            return new ResponseEntity<>(assembler.toModel(carrito), HttpStatus.OK);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // U
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar carrito", description = "Permite actualizar los datos de un carrito según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrito modificado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Carrito.class))),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado"),
            @ApiResponse(responseCode = "400", description = "JSON con mal formato"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El ID del carrito", example = "1")
    public ResponseEntity<EntityModel<Carrito>> updateCarrito(@PathVariable String id,
            @RequestBody @Valid Carrito carrito) {
        try {
            // Carrito updatedCarrito = carritoService.updateCarrito(id, carrito);
            // return new ResponseEntity<>(assembler.toModel(updatedCarrito),
            // HttpStatus.OK);
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
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
    @Operation(summary = "Eliminar carrito", description = "Elimina un carrito específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna el carrito eliminado"),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El ID del carrito", example = "1")
    public ResponseEntity<EntityModel<Carrito>> deleteCarrito(@PathVariable String id) {
        try {
            // Optional<Carrito> carritoOptional = carritoService.getCarritoById(id);
            // if (carritoOptional.isPresent()) {
            // Carrito carrito = carritoOptional.get();
            // carritoService.deleteCarrito(id);
            // return new ResponseEntity<>(assembler.toModel(carrito), HttpStatus.OK);
            // } else {
            // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            // }
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/artist/{artistId}")
    @Operation(summary = "Buscar carritos por artista", description = "Obtiene carritos según el ID del artista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna lista de carritos del artista"),
            @ApiResponse(responseCode = "404", description = "No se encuentran carritos para el artista", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El ID del artista", example = "1")
    public ResponseEntity<CollectionModel<EntityModel<Carrito>>> getCarritosByArtist(@PathVariable Long artistId) {
        try {
            // List<Carrito> carritos = carritoService.getCarritosByArtist(artistId);
            // if (carritos.isEmpty()) {
            // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            // } else {
            // return new ResponseEntity<>(assembler.toCollectionModel(carritos),
            // HttpStatus.OK);
            // }
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/format/{formatType}")
    @Operation(summary = "Buscar carritos por formato", description = "Obtiene carritos según el tipo de formato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna lista de carritos del formato"),
            @ApiResponse(responseCode = "404", description = "No se encuentran carritos para el formato", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El tipo de formato", example = "VINYL")
    public ResponseEntity<CollectionModel<EntityModel<Carrito>>> getCarritosByFormat(@PathVariable String formatType) {
        try {
            // List<Carrito> carritos = carritoService.getCarritosByFormat(formatType);
            // if (carritos.isEmpty()) {
            // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            // } else {
            // return new ResponseEntity<>(assembler.toCollectionModel(carritos),
            // HttpStatus.OK);
            // }
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{rutUsuario}/items")
    @Operation(summary = "Agregar item al carrito", description = "Agrega un producto al carrito del usuario")
    public ResponseEntity<EntityModel<Carrito>> agregarItem(@PathVariable Integer rutUsuario,
            @RequestBody ItemRequest request) {
        try {
            Carrito carrito = carritoService.agregarItemCarrito(rutUsuario, request.getSku(), request.getCantidad());
            return new ResponseEntity<>(assembler.toModel(carrito), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{rutUsuario}/items/{itemId}")
    @Operation(summary = "Actualizar cantidad de item", description = "Actualiza la cantidad de un item en el carrito")
    public ResponseEntity<EntityModel<Carrito>> actualizarItem(@PathVariable Integer rutUsuario,
            @PathVariable Integer itemId, @RequestBody ItemRequest request) {
        try {
            Carrito carrito = carritoService.actualizarCantidadItem(rutUsuario, itemId, request.getCantidad());
            return new ResponseEntity<>(assembler.toModel(carrito), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{rutUsuario}/items/{itemId}")
    @Operation(summary = "Eliminar item del carrito", description = "Elimina un item del carrito")
    public ResponseEntity<EntityModel<Carrito>> eliminarItem(@PathVariable Integer rutUsuario,
            @PathVariable Integer itemId) {
        try {
            Carrito carrito = carritoService.borrarItemCarrito(rutUsuario, itemId);
            return new ResponseEntity<>(assembler.toModel(carrito), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DTO simple para recibir datos del item
    public static class ItemRequest {
        private String sku;
        private Integer cantidad;

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }
    }
}