package fullstack.backend.assembler;

import fullstack.backend.controller.SelloController;
import fullstack.backend.model.Sello;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SelloModelAssembler implements RepresentationModelAssembler<Sello, EntityModel<Sello>> {

    @Override
    public EntityModel<Sello> toModel(Sello sello) {
        return EntityModel.of(sello,
                linkTo(methodOn(SelloController.class).getSelloById(sello.getId())).withSelfRel(),
                linkTo(methodOn(SelloController.class).getAllSellos()).withRel("sellos"),
                linkTo(methodOn(SelloController.class).updateSello(sello.getId(), sello)).withRel("update"),
                linkTo(methodOn(SelloController.class).deleteSello(sello.getId())).withRel("delete"));
    }
}
