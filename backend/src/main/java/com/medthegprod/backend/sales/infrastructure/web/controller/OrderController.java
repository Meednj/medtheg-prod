package com.medthegprod.backend.sales.infrastructure.web.controller;

import com.medthegprod.backend.identity.domain.model.UserId;
import com.medthegprod.backend.sales.application.usecase.CreateOrderUseCase;
import com.medthegprod.backend.sales.application.usecase.CreatePaymentUseCase;
import com.medthegprod.backend.sales.application.usecase.CreatePaymentUseCase.PaymentResult;
import com.medthegprod.backend.sales.application.usecase.ListCustomerOrdersUseCase;
import com.medthegprod.backend.sales.domain.model.Order;
import com.medthegprod.backend.sales.domain.model.OrderId;
import com.medthegprod.backend.sales.domain.model.OrderPage;
import com.medthegprod.backend.sales.infrastructure.web.AuthenticatedUser;
import com.medthegprod.backend.sales.infrastructure.web.dto.CreateOrderRequest;
import com.medthegprod.backend.sales.infrastructure.web.dto.OrderHistoryResponse;
import com.medthegprod.backend.sales.infrastructure.web.dto.OrderResponse;
import com.medthegprod.backend.sales.infrastructure.web.mapper.OrderWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Sales", description = "Customer orders and Stripe Checkout payments")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

        private final CreateOrderUseCase createOrderUseCase;
        private final ListCustomerOrdersUseCase listCustomerOrdersUseCase;
        private final CreatePaymentUseCase createPaymentUseCase;

        public OrderController(
                        CreateOrderUseCase createOrderUseCase,
                        ListCustomerOrdersUseCase listCustomerOrdersUseCase,
                        CreatePaymentUseCase createPaymentUseCase) {
                this.createOrderUseCase = createOrderUseCase;
                this.listCustomerOrdersUseCase = listCustomerOrdersUseCase;
                this.createPaymentUseCase = createPaymentUseCase;
        }

        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        @Operation(summary = "Create an order", description = "Creates an order for the authenticated customer.")
        public OrderResponse createOrder(
                        @Valid @RequestBody CreateOrderRequest request,
                        Authentication authentication) {
                UserId customerId = AuthenticatedUser.getUserId(authentication);

                List<CreateOrderUseCase.Item> items = request.items()
                                .stream()
                                .map(item -> new CreateOrderUseCase.Item(
                                                item.productId(),
                                                item.quantity()))
                                .toList();

                Order order = createOrderUseCase.execute(
                                customerId,
                                items);

                return OrderWebMapper.toResponse(order);
        }

        @GetMapping
        @Operation(summary = "List my orders", description = "Returns the authenticated customer's order history.")
        public OrderHistoryResponse getOrders(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "12") int size,
                        Authentication authentication) {
                UserId customerId = AuthenticatedUser.getUserId(authentication);

                OrderPage result = listCustomerOrdersUseCase.execute(
                                customerId,
                                page,
                                size);

                return OrderHistoryResponse.from(result);
        }

        @PostMapping("/{orderId}/payment")
        @Operation(summary = "Create a payment", description = "Creates a Stripe Checkout Session for an authenticated customer's order.")
        public PaymentResult createPayment(
                        @PathVariable UUID orderId,
                        Authentication authentication) {
                UserId customerId = AuthenticatedUser.getUserId(authentication);

                return createPaymentUseCase.execute(
                                new OrderId(orderId),
                                customerId.value());
        }

}