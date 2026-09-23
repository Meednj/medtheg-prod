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

@RestController
@RequestMapping("/api/orders")
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
        public static OrderHistoryResponse from(OrderPage page) {
                return new OrderHistoryResponse(
                                page.content()
                                                .stream()
                                                .map(OrderWebMapper::toResponse)
                                                .toList(),
                                page.page(),
                                page.size(),
                                page.totalElements(),
                                page.totalPages());
        }

        @PostMapping("/{orderId}/payment")
        public PaymentResult createPayment(
                        @PathVariable UUID orderId,
                        Authentication authentication) {
                UserId customerId = AuthenticatedUser.getUserId(authentication);

                return createPaymentUseCase.execute(
                                new OrderId(orderId),
                                customerId.value());
        }

}