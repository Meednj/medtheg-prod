CREATE TABLE entitlements (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    product_id UUID NOT NULL,
    order_id UUID NOT NULL,
    status VARCHAR(50) NOT NULL,
    granted_at TIMESTAMP WITH TIME ZONE NOT NULL,
    revoked_at TIMESTAMP WITH TIME ZONE,

    CONSTRAINT uk_entitlements_customer_product
        UNIQUE (customer_id, product_id),

    CONSTRAINT fk_entitlements_order
        FOREIGN KEY (order_id)
        REFERENCES orders(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_entitlements_customer_id
    ON entitlements(customer_id);

CREATE INDEX idx_entitlements_product_id
    ON entitlements(product_id);

CREATE INDEX idx_entitlements_order_id
    ON entitlements(order_id);