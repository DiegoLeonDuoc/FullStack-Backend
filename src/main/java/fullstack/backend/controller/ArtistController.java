package fullstack.backend.controller;

import fullstack.backend.assembler.ArtistModelAssembler;
import fullstack.backend.model.Artist;
import fullstack.backend.service.ArtistService;
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
@RequestMapping("/api/v1/artists")
@Tag(name = "Controlador Artista", description = "Servicios de gestión de artistas")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ArtistModelAssembler assembler;

    // C
    @PostMapping
    @Operation(summary = "Agregar Artista", description = "Permite registrar un artista en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Artista creado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Artist.class))),
            @ApiResponse(responseCode = "400", description = "JSON con mal formato o artista duplicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<Artist>> createArtist(@RequestBody @Valid Artist artist) {
        Artist createdArtist = artistService.createArtist(artist);
        return new ResponseEntity<>(assembler.toModel(createdArtist), HttpStatus.CREATED);
    }

    // R
    @GetMapping
    @Operation(summary = "Obtener artistas", description = "Obtiene la lista de artistas registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna lista completa de artistas"),
            @ApiResponse(responseCode = "404", description = "No se encuentran artistas", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CollectionModel<EntityModel<Artist>>> getAllArtists() {
        List<Artist> artists = artistService.getAllArtists();
        if (artists.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(assembler.toCollectionModel(artists), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar artista por ID", description = "Obtiene un artista según el ID registrado en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna Artista"),
            @ApiResponse(responseCode = "404", description = "Artista no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El ID del artista", example = "1")
    public ResponseEntity<EntityModel<Artist>> getArtistById(@PathVariable Long id) {
        Optional<Artist> artistOptional = artistService.getArtistById(id);
        if (artistOptional.isPresent()) {
            return new ResponseEntity<>(assembler.toModel(artistOptional.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Buscar artista por nombre", description = "Obtiene un artista según el nombre registrado en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna Artista"),
            @ApiResponse(responseCode = "404", description = "Artista no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El nombre del artista", example = "The Beatles")
    public ResponseEntity<EntityModel<Artist>> getArtistByName(@PathVariable String name) {
        Optional<Artist> artistOptional = artistService.getArtistByName(name);
        if (artistOptional.isPresent()) {
            return new ResponseEntity<>(assembler.toModel(artistOptional.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // U
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar artista", description = "Permite actualizar los datos de un artista según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artista modificado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Artist.class))),
            @ApiResponse(responseCode = "404", description = "Artista no encontrado"),
            @ApiResponse(responseCode = "400", description = "JSON con mal formato o artista duplicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El ID del artista", example = "1")
    public ResponseEntity<EntityModel<Artist>> updateArtist(@PathVariable Long id, @RequestBody @Valid Artist artist) {
        Artist updatedArtist = artistService.updateArtist(id, artist);
        return new ResponseEntity<>(assembler.toModel(updatedArtist), HttpStatus.OK);
    }

    // D
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar artista", description = "Elimina un artista específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna el artista eliminado"),
            @ApiResponse(responseCode = "404", description = "Artista no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El ID del artista", example = "1")
    public ResponseEntity<EntityModel<Artist>> deleteArtist(@PathVariable Long id) {
        Optional<Artist> artistOptional = artistService.getArtistById(id);
        if (artistOptional.isPresent()) {
            Artist artist = artistOptional.get();
            artistService.deleteArtist(id);
            return new ResponseEntity<>(assembler.toModel(artist), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}