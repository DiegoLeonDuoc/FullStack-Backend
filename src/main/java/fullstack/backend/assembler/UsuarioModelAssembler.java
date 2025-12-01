package fullstack.backend.assembler;

import fullstack.backend.controller.UsuarioController;
import fullstack.backend.model.Usuario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {

    @Override
    public EntityModel<Usuario> toModel(Usuario usuario) {
        return EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).obtenerUsuarioPorRut(usuario.getRut())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).obtenerUsuarios()).withRel("usuarios"),
                linkTo(methodOn(UsuarioController.class).actualizarUsuario(usuario.getRut(), usuario)).withRel("update"),
                linkTo(methodOn(UsuarioController.class).deleteUser(usuario.getRut())).withRel("delete")
        );
    }
}
