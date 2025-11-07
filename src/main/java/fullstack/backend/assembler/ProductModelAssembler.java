package fullstack.backend.assembler;

import fullstack.backend.controller.ProductController;
import fullstack.backend.model.Product;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductModelAssembler implements RepresentationModelAssembler<Product, EntityModel<Product>> {

    @Override
    public EntityModel<Product> toModel(Product artist) {
        return EntityModel.of(artist,
                linkTo(methodOn(ProductController.class).getProductById(artist.getProductId())).withSelfRel(),
                linkTo(methodOn(ProductController.class).getAllProducts()).withRel("products"),
                linkTo(methodOn(ProductController.class).updateProduct(artist.getProductId(), artist)).withRel("update"),
                linkTo(methodOn(ProductController.class).deleteProduct(artist.getProductId())).withRel("delete")
        );
    }
}