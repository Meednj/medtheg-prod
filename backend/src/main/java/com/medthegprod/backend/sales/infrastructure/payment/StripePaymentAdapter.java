package com.medthegprod.backend.sales.infrastructure.payment;

import com.medthegprod.backend.sales.application.port.PaymentGateway;
import com.medthegprod.backend.sales.domain.model.Order;
import com.medthegprod.backend.sales.domain.model.OrderItem;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Component;

@Component
public class StripePaymentAdapter implements PaymentGateway {

    private final StripeProperties properties;

    public StripePaymentAdapter(StripeProperties properties) {
        this.properties = properties;

        Stripe.apiKey = properties.secretKey();
    }

    @Override
    public PaymentSession createPayment(Order order) {

        SessionCreateParams.Builder builder = SessionCreateParams.builder()
                .setMode(
                        SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(
                        properties.successUrl()
                                + "?orderId="
                                + order.getId().value())
                .setCancelUrl(
                        properties.cancelUrl()
                                + "?orderId="
                                + order.getId().value())
                .putMetadata(
                        "orderId",
                        order.getId().value().toString());

        for (OrderItem item : order.getItems()) {

            SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData
                    .builder()
                    .setName(item.getProductTitle())
                    .build();

            SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                    .setCurrency(
                            item.getUnitPrice()
                                    .currency()
                                    .getCurrencyCode()
                                    .toLowerCase())
                    .setUnitAmount(
                            item.getUnitPrice()
                                    .amount()
                                    .movePointRight(2)
                                    .longValueExact())
                    .setProductData(productData)
                    .build();

            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setPriceData(priceData)
                    .setQuantity(
                            (long) item.getQuantity())
                    .build();

            builder.addLineItem(lineItem);
        }

        try {
            Session session = Session.create(builder.build());

            return new PaymentSession(
                    session.getPaymentIntent(),
                    session.getId(),
                    session.getUrl());

        } catch (StripeException e) {
            throw new IllegalStateException(
                    "Unable to create Stripe checkout session",
                    e);
        }
    }
}