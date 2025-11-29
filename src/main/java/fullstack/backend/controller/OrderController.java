package fullstack.backend.controller;

import fullstack.backend.assembler.OrderModelAssembler;
import fullstack.backend.dto.OrderRequestDto;
import fullstack.backend.dto.OrderResponseDto;
import fullstack.backend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Controlador Órdenes", description = "Servicios de gestión de órdenes de compra")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderModelAssembler assembler;

    @PostMapping
    @Operation(summary = "Crear orden", description = "Registra una orden de compra")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orden creada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Ejemplo de payload para crear una orden",
            required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderRequestDto.class),
                    examples = @ExampleObject(value = "{\n  \"customerId\": 1,\n  \"productId\": \"abbey-road-vinilo\",\n  \"quantity\": 2,\n  \"status\": \"CREATED\",\n  \"responsibleUserId\": 2\n}")))
    public ResponseEntity<EntityModel<OrderResponseDto>> createOrder(@RequestBody @Valid OrderRequestDto orderRequestDto) {
        OrderResponseDto created = orderService.createOrder(orderRequestDto);
        return new ResponseEntity<>(assembler.toModel(created), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Obtener órdenes", description = "Obtiene todas las órdenes registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de órdenes"),
            @ApiResponse(responseCode = "404", description = "No se encuentran órdenes", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CollectionModel<EntityModel<OrderResponseDto>>> getAllOrders() {
        List<OrderResponseDto> orders = orderService.getAllOrders();
        if (orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(assembler.toCollectionModel(orders), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar orden por ID", description = "Obtiene una orden por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden encontrada"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "ID de la orden", example = "1")
    public ResponseEntity<EntityModel<OrderResponseDto>> getOrderById(@PathVariable Long id) {
        Optional<OrderResponseDto> order = orderService.getOrderById(id);
        return order.map(orderResponseDto -> new ResponseEntity<>(assembler.toModel(orderResponseDto), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar orden", description = "Actualiza los datos de una orden")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden actualizada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Ejemplo de payload para actualizar una orden",
            required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderRequestDto.class),
                    examples = @ExampleObject(value = "{\n  \"customerId\": 1,\n  \"productId\": \"abbey-road-vinilo\",\n  \"quantity\": 3,\n  \"status\": \"IN_PROGRESS\",\n  \"responsibleUserId\": 2\n}")))
    public ResponseEntity<EntityModel<OrderResponseDto>> updateOrder(@PathVariable Long id, @RequestBody @Valid OrderRequestDto orderRequestDto) {
        OrderResponseDto updated = orderService.updateOrder(id, orderRequestDto);
        return new ResponseEntity<>(assembler.toModel(updated), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar orden", description = "Elimina una orden específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden eliminada"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameter(description = "ID de la orden", example = "1")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/filter")
    @Operation(summary = "Filtrar órdenes", description = "Filtra órdenes por rango de fecha, estado y responsable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultados del filtro"),
            @ApiResponse(responseCode = "404", description = "No se encuentran órdenes", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CollectionModel<EntityModel<OrderResponseDto>>> filterOrders(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @Parameter(description = "Fecha de inicio", example = "2025-01-01T00:00:00") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @Parameter(description = "Fecha de término", example = "2025-01-31T23:59:59") LocalDateTime endDate,
            @RequestParam(required = false) @Parameter(description = "Estado de la orden", example = "CREATED") String status,
            @RequestParam(required = false) @Parameter(description = "ID del responsable", example = "2") Long responsibleId
    ) {
        List<OrderResponseDto> orders = orderService.filterOrders(startDate, endDate, status, responsibleId);
        if (orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(assembler.toCollectionModel(orders), HttpStatus.OK);
    }
}
