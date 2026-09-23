package com.medthegprod.backend.sales.application.service;

import com.medthegprod.backend.catalog.application.usecase.GetProductUseCase;
import com.medthegprod.backend.catalog.domain.model.Money;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductCategory;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.domain.model.ProductType;
import com.medthegprod.backend.identity.domain.model.UserId;
import com.medthegprod.backend.sales.application.usecase.CreateOrderUseCase;
import com.medthegprod.backend.sales.domain.model.Order;
import com.medthegprod.backend.sales.domain.model.OrderItem;
import com.medthegprod.backend.sales.domain.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateOrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private GetProductUseCase getProductUseCase;

    private CreateOrderService service;

    @BeforeEach
    void setUp() {
        service = new CreateOrderService(
                orderRepository,
                getProductUseCase);
    }

    @Test
    void shouldCreateOrderUsingCatalogProductData() {

        UserId customerId = UserId.generate();
        ProductId productId = ProductId.generate();

        Product product = new Product(
                productId,
                "Dark Beat",
                "Dark underground beat",
                ProductType.BEAT,
                Money.eur(new BigDecimal("15.00")));

        product.addCategory(ProductCategory.BEATS);
        product.publish();

        when(getProductUseCase.execute(productId))
                .thenReturn(product);

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order order = service.execute(
                customerId,
                List.of(
                        new CreateOrderUseCase.Item(
                                productId.value(),
                                1)));

        assertNotNull(order);
        assertEquals(customerId, order.getCustomerId());
        assertEquals(1, order.getItems().size());

        OrderItem item = order.getItems().get(0);

        assertEquals(productId, item.getProductId());
        assertEquals("Dark Beat", item.getProductTitle());

        assertEquals(
                new BigDecimal("15.00"),
                item.getUnitPrice().amount());

        assertEquals(1, item.getQuantity());

        assertEquals(
                new BigDecimal("15.00"),
                order.total().amount());

        verify(getProductUseCase).execute(productId);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldRejectEmptyOrder() {

        UserId customerId = UserId.generate();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.execute(
                        customerId,
                        List.of()));

        verify(getProductUseCase, never()).execute(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldRejectNullCustomer() {

        ProductId productId = ProductId.generate();

        assertThrows(
                NullPointerException.class,
                () -> service.execute(
                        null,
                        List.of(
                                new CreateOrderUseCase.Item(
                                        productId.value(),
                                        1))));

        verify(getProductUseCase, never()).execute(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldCreateOrderWithMultipleProducts() {

        UserId customerId = UserId.generate();

        ProductId firstProductId = ProductId.generate();
        ProductId secondProductId = ProductId.generate();

        Product firstProduct = new Product(
                firstProductId,
                "Dark Beat",
                "Dark beat",
                ProductType.BEAT,
                Money.eur(new BigDecimal("15.00")));
        firstProduct.addCategory(ProductCategory.BEATS);
        firstProduct.publish();

        Product secondProduct = new Product(
                secondProductId,
                "Drum Kit",
                "Premium drums",
                ProductType.DRUM_KIT,
                Money.eur(new BigDecimal("20.00")));

        secondProduct.addCategory(ProductCategory.KITS);
        secondProduct.publish();

        when(getProductUseCase.execute(firstProductId))
                .thenReturn(firstProduct);

        when(getProductUseCase.execute(secondProductId))
                .thenReturn(secondProduct);

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order order = service.execute(
                customerId,
                List.of(
                        new CreateOrderUseCase.Item(
                                firstProductId.value(),
                                1),
                        new CreateOrderUseCase.Item(
                                secondProductId.value(),
                                2)));

        assertEquals(2, order.getItems().size());

        assertEquals(
                new BigDecimal("55.00"),
                order.total().amount());

        verify(getProductUseCase).execute(firstProductId);
        verify(getProductUseCase).execute(secondProductId);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldRejectUnpublishedProduct() {

        UserId customerId = UserId.generate();
        ProductId productId = ProductId.generate();

        Product product = new Product(
                productId,
                "Draft Beat",
                "Not published yet",
                ProductType.BEAT,
                Money.eur(new BigDecimal("15.00")));

        when(getProductUseCase.execute(productId))
                .thenReturn(product);

        assertThrows(
                IllegalStateException.class,
                () -> service.execute(
                        customerId,
                        List.of(
                                new CreateOrderUseCase.Item(
                                        productId.value(),
                                        1))));

        verify(orderRepository, never()).save(any());
    }
}
