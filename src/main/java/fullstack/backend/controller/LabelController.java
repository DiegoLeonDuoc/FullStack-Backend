package fullstack.backend.controller;

import fullstack.backend.assembler.LabelModelAssembler;
import fullstack.backend.model.Label;
import fullstack.backend.service.LabelService;
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
@RequestMapping("/api/v1/labels")
@Tag(name = "Controlador Sello Discográfico", description = "Servicios de gestión de sellos discográficos")
public class LabelController {

    @Autowired
    private LabelService labelService;

    @Autowired
    private LabelModelAssembler assembler;

    // C
    @PostMapping
    @Operation(summary = "Agregar Sello Discográfico", description = "Permite registrar un sello discográfico en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sello discográfico creado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Label.class))),
            @ApiResponse(responseCode = "400", description = "JSON con mal formato o sello duplicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<Label>> createLabel(@RequestBody @Valid Label label) {
        Label createdLabel = labelService.createLabel(label);
        return new ResponseEntity<>(assembler.toModel(createdLabel), HttpStatus.CREATED);
    }

    // R
    @GetMapping
    @Operation(summary = "Obtener sellos discográficos", description = "Obtiene la lista de sellos discográficos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna lista completa de sellos discográficos"),
            @ApiResponse(responseCode = "404", description = "No se encuentran sellos discográficos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CollectionModel<EntityModel<Label>>> getAllLabels() {
        List<Label> labels = labelService.getAllLabels();
        if (labels.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(assembler.toCollectionModel(labels), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sello discográfico por ID", description = "Obtiene un sello discográfico según el ID registrado en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna Sello Discográfico"),
            @ApiResponse(responseCode = "404", description = "Sello discográfico no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El ID del sello discográfico", example = "1")
    public ResponseEntity<EntityModel<Label>> getLabelById(@PathVariable Long id) {
        Optional<Label> labelOptional = labelService.getLabelById(id);
        if (labelOptional.isPresent()) {
            return new ResponseEntity<>(assembler.toModel(labelOptional.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Buscar sello discográfico por nombre", description = "Obtiene un sello discográfico según el nombre registrado en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna Sello Discográfico"),
            @ApiResponse(responseCode = "404", description = "Sello discográfico no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El nombre del sello discográfico", example = "Beat Bazaar Records")
    public ResponseEntity<EntityModel<Label>> getLabelByName(@PathVariable String name) {
        Optional<Label> labelOptional = labelService.getLabelByName(name);
        if (labelOptional.isPresent()) {
            return new ResponseEntity<>(assembler.toModel(labelOptional.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // U
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar sello discográfico", description = "Permite actualizar los datos de un sello discográfico según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sello discográfico modificado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Label.class))),
            @ApiResponse(responseCode = "404", description = "Sello discográfico no encontrado"),
            @ApiResponse(responseCode = "400", description = "JSON con mal formato o sello duplicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El ID del sello discográfico", example = "1")
    public ResponseEntity<EntityModel<Label>> updateLabel(@PathVariable Long id, @RequestBody @Valid Label label) {
        Label updatedLabel = labelService.updateLabel(id, label);
        return new ResponseEntity<>(assembler.toModel(updatedLabel), HttpStatus.OK);
    }

    // D
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar sello discográfico", description = "Elimina un sello discográfico específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna el sello discográfico eliminado"),
            @ApiResponse(responseCode = "404", description = "Sello discográfico no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El ID del sello discográfico", example = "1")
    public ResponseEntity<EntityModel<Label>> deleteLabel(@PathVariable Long id) {
        Optional<Label> labelOptional = labelService.getLabelById(id);
        if (labelOptional.isPresent()) {
            Label label = labelOptional.get();
            labelService.deleteLabel(id);
            return new ResponseEntity<>(assembler.toModel(label), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}