package fullstack.backend.assembler;

import fullstack.backend.controller.CarritoController;
import fullstack.backend.model.Carrito;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CarritoModelAssembler implements RepresentationModelAssembler<Carrito, EntityModel<Carrito>> {

    @Override
    public EntityModel<Carrito> toModel(Carrito carrito) {
        return EntityModel.of(carrito,
                linkTo(methodOn(CarritoController.class).obtenerCarritoPorRut(carrito.getUsuario().getRut()))
                        .withSelfRel()
        // linkTo(methodOn(CarritoController.class).obtenerCarritos()).withRel("carritos"),
        // linkTo(methodOn(CarritoController.class).updateCarrito(carrito.getId(),
        // carrito)).withRel("update"),
        // linkTo(methodOn(CarritoController.class).deleteCarrito(carrito.getId())).withRel("delete")
        );
    }
}