package fullstack.backend.assembler;

import fullstack.backend.controller.OrderController;
import fullstack.backend.dto.OrderResponseDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<OrderResponseDto, EntityModel<OrderResponseDto>> {

    @Override
    public EntityModel<OrderResponseDto> toModel(OrderResponseDto order) {
        return EntityModel.of(order,
                linkTo(methodOn(OrderController.class).getOrderById(order.getOrderId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).getAllOrders()).withRel("orders"),
                linkTo(methodOn(OrderController.class).updateOrder(order.getOrderId(), null)).withRel("update"),
                linkTo(methodOn(OrderController.class).deleteOrder(order.getOrderId())).withRel("delete")
        );
    }
}
