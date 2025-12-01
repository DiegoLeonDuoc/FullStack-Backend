package fullstack.backend.assembler;

import fullstack.backend.controller.ArtistaController;
import fullstack.backend.model.Artista;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ArtistaModelAssembler implements RepresentationModelAssembler<Artista, EntityModel<Artista>> {

    @Override
    public EntityModel<Artista> toModel(Artista artista) {
        return EntityModel.of(artista,
                linkTo(methodOn(ArtistaController.class).getArtistaById(artista.getId())).withSelfRel(),
                linkTo(methodOn(ArtistaController.class).getAllArtistas()).withRel("artistas"),
                linkTo(methodOn(ArtistaController.class).updateArtista(artista.getId(), artista)).withRel("update"),
                linkTo(methodOn(ArtistaController.class).deleteArtista(artista.getId())).withRel("delete"));
    }
}
