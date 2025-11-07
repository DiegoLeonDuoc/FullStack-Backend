package fullstack.backend.assembler;

import fullstack.backend.controller.ArtistController;
import fullstack.backend.model.Artist;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ArtistModelAssembler implements RepresentationModelAssembler<Artist, EntityModel<Artist>> {

    @Override
    public EntityModel<Artist> toModel(Artist artist) {
        return EntityModel.of(artist,
                linkTo(methodOn(ArtistController.class).getArtistById(artist.getArtistId())).withSelfRel(),
                linkTo(methodOn(ArtistController.class).getAllArtists()).withRel("artists"),
                linkTo(methodOn(ArtistController.class).updateArtist(artist.getArtistId(), artist)).withRel("update"),
                linkTo(methodOn(ArtistController.class).deleteArtist(artist.getArtistId())).withRel("delete")
        );
    }
}