package fullstack.backend.controller;

import fullstack.backend.assembler.UsuarioModelAssembler;
import fullstack.backend.model.Usuario;
import fullstack.backend.service.UsuarioService;
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
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Controlador Usuario", description = "Servicios de gestión de usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioModelAssembler assembler;

    // C
    @PostMapping
    @Operation(summary = "Agregar Usuario", description = "Permite registrar un usuario en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "400", description = "JSON con mal formato o datos duplicados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<Usuario>> crearUsuario(@RequestBody @Valid Usuario usuario) {
        try {
            Usuario usuarioCreado = usuarioService.crearUsuario(usuario);
            return new ResponseEntity<>(assembler.toModel(usuarioCreado), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // R
    @GetMapping
    @Operation(summary = "Obtener usuarios", description = "Obtiene la lista de usuarios registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna lista completa de usuarios"),
            @ApiResponse(responseCode = "404", description = "No se encuentran usuarios", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> obtenerUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.obtenerUsuarios();
            if (usuarios.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(assembler.toCollectionModel(usuarios), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{rut}")
    @Operation(summary = "Buscar usuario por RUT", description = "Obtiene un usuario según el RUT registrado en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna Usuario"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El RUT del usuario sin dígito verificador", example = "12345678")
    public ResponseEntity<EntityModel<Usuario>> obtenerUsuarioPorRut(@PathVariable Integer rut) {
        try {
            Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuarioPorRut(rut);
            if (usuarioOptional.isPresent()) {
                return new ResponseEntity<>(assembler.toModel(usuarioOptional.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar usuario por email", description = "Obtiene un usuario según el email registrado en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna Usuario"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El email del usuario", example = "usuario@example.com")
    public ResponseEntity<EntityModel<Usuario>> obtenerUsuarioPorRut(@PathVariable String email) {
        try {
            Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuarioPorEmail(email);
            if (usuarioOptional.isPresent()) {
                return new ResponseEntity<>(assembler.toModel(usuarioOptional.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    // U
    @PutMapping("/{rut}")
    @Operation(summary = "Actualizar usuario", description = "Permite actualizar los datos de un usuario según su RUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario modificado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "400", description = "JSON con mal formato o datos duplicados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El ID del usuario", example = "1")
    public ResponseEntity<EntityModel<Usuario>> actualizarUsuario(@PathVariable Integer rut, @RequestBody @Valid Usuario usuario) {
        try {
            Usuario usuarioActualizado = usuarioService.actualizarUsuario(rut, usuario);
            return new ResponseEntity<>(assembler.toModel(usuarioActualizado), HttpStatus.OK);
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
    @DeleteMapping("/{rut}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna el usuario eliminado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "El ID del usuario", example = "1")
    public ResponseEntity<EntityModel<Usuario>> deleteUser(@PathVariable Integer rut) {
        try {
            Optional<Usuario> userOptional = usuarioService.obtenerUsuarioPorRut(rut);
            if (userOptional.isPresent()) {
                Usuario usuario = userOptional.get();
                usuarioService.borrarUsuario(rut);
                return new ResponseEntity<>(assembler.toModel(usuario), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}