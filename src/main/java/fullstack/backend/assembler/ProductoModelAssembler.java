package fullstack.backend.assembler;

import fullstack.backend.controller.ProductoController;
import fullstack.backend.model.Producto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<Producto, EntityModel<Producto>> {

    @Override
    public EntityModel<Producto> toModel(Producto producto) {
        return EntityModel.of(producto,
                linkTo(methodOn(ProductoController.class).getProductoById(producto.getSku())).withSelfRel(),
                linkTo(methodOn(ProductoController.class).getAllProductos()).withRel("productos"),
                linkTo(methodOn(ProductoController.class).updateProducto(producto.getSku(), producto))
                        .withRel("update"),
                linkTo(methodOn(ProductoController.class).deleteProducto(producto.getSku())).withRel("delete"));
    }
}
