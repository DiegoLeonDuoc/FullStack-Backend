package fullstack.backend.controller;

import fullstack.backend.assembler.SelloModelAssembler;
import fullstack.backend.model.Sello;
import fullstack.backend.service.SelloService;
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
@RequestMapping("/api/v1/sellos")
@Tag(name = "Controlador Sello Discográfico", description = "Servicios de gestión de sellos discográficos")
public class SelloController {

    @Autowired
    private SelloService selloService;

    @Autowired
    private SelloModelAssembler assembler;

    // C
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Agregar Sello Discográfico", description = "Permite registrar un sello discográfico en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sello discográfico creado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Sello.class))),
            @ApiResponse(responseCode = "400", description = "JSON con mal formato o sello duplicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<Sello>> createSello(@RequestBody @Valid Sello sello) {
        try {
            Sello createdSello = selloService.createSello(sello);
            return new ResponseEntity<>(assembler.toModel(createdSello), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // R
    @GetMapping
    @Operation(summary = "Obtener sellos discográficos", description = "Obtiene la lista de sellos discográficos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna lista completa de sellos discográficos"),
            @ApiResponse(responseCode = "404", description = "No se encuentran sellos discográficos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CollectionModel<EntityModel<Sello>>> getAllSellos() {
        try {
            List<Sello> sellos = selloService.getAllSellos();
            if (sellos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(assembler.toCollectionModel(sellos), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sello discográfico por ID", description = "Obtiene un sello discográfico según el ID registrado en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna Sello Discográfico"),
            @ApiResponse(responseCode = "404", description = "Sello discográfico no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El ID del sello discográfico", example = "1")
    public ResponseEntity<EntityModel<Sello>> getSelloById(@PathVariable Long id) {
        try {
            Optional<Sello> selloOptional = selloService.getSelloById(id);
            if (selloOptional.isPresent()) {
                return new ResponseEntity<>(assembler.toModel(selloOptional.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar sello discográfico por nombre", description = "Obtiene un sello discográfico según el nombre registrado en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna Sello Discográfico"),
            @ApiResponse(responseCode = "404", description = "Sello discográfico no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El nombre del sello discográfico", example = "Beat Bazaar Records")
    public ResponseEntity<EntityModel<Sello>> getSelloByNombre(@RequestParam String nombre) {
        try {
            Optional<Sello> selloOptional = selloService.getSelloByNombre(nombre);
            if (selloOptional.isPresent()) {
                return new ResponseEntity<>(assembler.toModel(selloOptional.get()), HttpStatus.OK);
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
    @Operation(summary = "Actualizar sello discográfico", description = "Permite actualizar los datos de un sello discográfico según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sello discográfico modificado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Sello.class))),
            @ApiResponse(responseCode = "404", description = "Sello discográfico no encontrado"),
            @ApiResponse(responseCode = "400", description = "JSON con mal formato o sello duplicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El ID del sello discográfico", example = "1")
    public ResponseEntity<EntityModel<Sello>> updateSello(@PathVariable Long id, @RequestBody @Valid Sello sello) {
        try {
            Sello updatedSello = selloService.updateSello(id, sello);
            return new ResponseEntity<>(assembler.toModel(updatedSello), HttpStatus.OK);
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
    @Operation(summary = "Eliminar sello discográfico", description = "Elimina un sello discográfico específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna el sello discográfico eliminado"),
            @ApiResponse(responseCode = "404", description = "Sello discográfico no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El ID del sello discográfico", example = "1")
    public ResponseEntity<EntityModel<Sello>> deleteSello(@PathVariable Long id) {
        try {
            Optional<Sello> selloOptional = selloService.getSelloById(id);
            if (selloOptional.isPresent()) {
                Sello sello = selloOptional.get();
                selloService.deleteSello(id);
                return new ResponseEntity<>(assembler.toModel(sello), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
