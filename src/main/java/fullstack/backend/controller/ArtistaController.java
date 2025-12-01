package fullstack.backend.controller;

import fullstack.backend.assembler.ArtistaModelAssembler;
import fullstack.backend.model.Artista;
import fullstack.backend.service.ArtistaService;
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
@RequestMapping("/api/v1/artistas")
@Tag(name = "Controlador Artista", description = "Servicios de gestión de artistas")
public class ArtistaController {

    @Autowired
    private ArtistaService artistaService;

    @Autowired
    private ArtistaModelAssembler assembler;

    // C
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Agregar Artista", description = "Permite registrar un artista en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Artista creado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Artista.class))),
            @ApiResponse(responseCode = "400", description = "JSON con mal formato o artista duplicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<Artista>> createArtista(@RequestBody @Valid Artista artista) {
        try {
            Artista createdArtista = artistaService.createArtista(artista);
            return new ResponseEntity<>(assembler.toModel(createdArtista), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // R
    @GetMapping
    @Operation(summary = "Obtener artistas", description = "Obtiene la lista de artistas registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna lista completa de artistas"),
            @ApiResponse(responseCode = "404", description = "No se encuentran artistas", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CollectionModel<EntityModel<Artista>>> getAllArtistas() {
        try {
            List<Artista> artistas = artistaService.getAllArtistas();
            if (artistas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(assembler.toCollectionModel(artistas), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar artista por ID", description = "Obtiene un artista según el ID registrado en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna Artista"),
            @ApiResponse(responseCode = "404", description = "Artista no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El ID del artista", example = "1")
    public ResponseEntity<EntityModel<Artista>> getArtistaById(@PathVariable Long id) {
        try {
            Optional<Artista> artistaOptional = artistaService.getArtistaById(id);
            if (artistaOptional.isPresent()) {
                return new ResponseEntity<>(assembler.toModel(artistaOptional.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar artista por nombre", description = "Obtiene un artista según el nombre registrado en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna Artista"),
            @ApiResponse(responseCode = "404", description = "Artista no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El nombre del artista", example = "The Beatles")
    public ResponseEntity<EntityModel<Artista>> getArtistaByNombre(@PathVariable String nombre) {
        try {
            Optional<Artista> artistaOptional = artistaService.getArtistaByNombre(nombre);
            if (artistaOptional.isPresent()) {
                return new ResponseEntity<>(assembler.toModel(artistaOptional.get()), HttpStatus.OK);
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
    @Operation(summary = "Actualizar artista", description = "Permite actualizar los datos de un artista según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artista modificado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Artista.class))),
            @ApiResponse(responseCode = "404", description = "Artista no encontrado"),
            @ApiResponse(responseCode = "400", description = "JSON con mal formato o artista duplicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El ID del artista", example = "1")
    public ResponseEntity<EntityModel<Artista>> updateArtista(@PathVariable Long id,
            @RequestBody @Valid Artista artista) {
        try {
            Artista updatedArtista = artistaService.updateArtista(id, artista);
            return new ResponseEntity<>(assembler.toModel(updatedArtista), HttpStatus.OK);
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
    @Operation(summary = "Eliminar artista", description = "Elimina un artista específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna el artista eliminado"),
            @ApiResponse(responseCode = "404", description = "Artista no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El ID del artista", example = "1")
    public ResponseEntity<EntityModel<Artista>> deleteArtista(@PathVariable Long id) {
        try {
            Optional<Artista> artistaOptional = artistaService.getArtistaById(id);
            if (artistaOptional.isPresent()) {
                Artista artista = artistaOptional.get();
                artistaService.deleteArtista(id);
                return new ResponseEntity<>(assembler.toModel(artista), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
