package fullstack.backend.assembler;

import fullstack.backend.controller.LabelController;
import fullstack.backend.model.Label;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LabelModelAssembler implements RepresentationModelAssembler<Label, EntityModel<Label>> {

    @Override
    public EntityModel<Label> toModel(Label artist) {
        return EntityModel.of(artist,
                linkTo(methodOn(LabelController.class).getLabelById(artist.getLabelId())).withSelfRel(),
                linkTo(methodOn(LabelController.class).getAllLabels()).withRel("labels"),
                linkTo(methodOn(LabelController.class).updateLabel(artist.getLabelId(), artist)).withRel("update"),
                linkTo(methodOn(LabelController.class).deleteLabel(artist.getLabelId())).withRel("delete")
        );
    }
}